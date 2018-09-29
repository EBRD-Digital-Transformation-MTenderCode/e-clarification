package com.procurement.clarification.service

import com.procurement.clarification.dao.EnquiryDao
import com.procurement.clarification.dao.PeriodDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType.*
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.ocds.*
import com.procurement.clarification.model.dto.request.AddAnswerRq
import com.procurement.clarification.model.dto.request.CreateEnquiryRq
import com.procurement.clarification.model.dto.request.IdentifierCreate
import com.procurement.clarification.model.dto.request.OrganizationReferenceCreate
import com.procurement.clarification.model.dto.response.AddAnswerRs
import com.procurement.clarification.model.dto.response.CheckAnswerRs
import com.procurement.clarification.model.dto.response.CheckEnquiresRs
import com.procurement.clarification.model.dto.response.CreateEnquiryRs
import com.procurement.clarification.model.entity.EnquiryEntity
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.toLocal
import com.procurement.clarification.utils.toObject
import org.springframework.stereotype.Service
import java.util.*

interface EnquiryService {

    fun createEnquiry(cm: CommandMessage): ResponseDto

    fun addAnswer(cm: CommandMessage): ResponseDto

    fun checkAnswer(cm: CommandMessage): ResponseDto

    fun checkEnquiries(cm: CommandMessage): ResponseDto
}

@Service
class EnquiryServiceImpl(private val generationService: GenerationService,
                         private val enquiryDao: EnquiryDao,
                         private val periodDao: PeriodDao,
                         private val periodService: PeriodService) : EnquiryService {

    override fun createEnquiry(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)
        val dto = toObject(CreateEnquiryRq::class.java, cm.data)

        periodService.checkDateInPeriod(dateTime, cpId, stage)
        val enquiryRequest = dto.enquiry
        val periodEntity = periodDao.getByCpIdAndStage(cpId, stage)
        val owner = periodEntity.owner
        val author = converterOrganizationReferenceCreateToOrganizationReference(enquiryRequest.author)
        val enquiry = Enquiry(
                id = generationService.generateTimeBasedUUID().toString(),
                date = dateTime,
                author = author,
                title = enquiryRequest.title,
                description = enquiryRequest.description,
                answer = null,
                relatedItem = enquiryRequest.relatedItem,
                relatedLot = enquiryRequest.relatedLot,
                dateAnswered = null
        )

        val entity = getEntity(
                cpId = cpId,
                token = generationService.generateRandomUUID(),
                stage = stage,
                isAnswered = false,
                enquiry = enquiry
        )
        enquiryDao.save(entity)
        return ResponseDto(data = CreateEnquiryRs(entity.token.toString(), owner, enquiry))
    }

    override fun checkEnquiries(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)
        val enquiryPeriod = periodService.getPeriodEntity(cpId, stage)
        val startDate = enquiryPeriod.startDate.toLocal()
        val endDate = enquiryPeriod.endDate.toLocal()
        val isEnquiryPeriodExpired = (dateTime >= endDate)
        val isAllAnswered = checkIsAllAnswered(cpId, stage)
        return ResponseDto(data = CheckEnquiresRs(
                isEnquiryPeriodExpired = isEnquiryPeriodExpired,
                tender = Tender(Period(startDate, endDate)),
                allAnswered = isAllAnswered))
    }

    override fun addAnswer(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val token = cm.context.token ?: throw ErrorException(CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val enquiryId = cm.context.id ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)
        val dto = toObject(AddAnswerRq::class.java, cm.data)

        val entity = enquiryDao.getByCpIdAndStageAndToken(cpId, stage, UUID.fromString(token))
        val periodEntity = periodDao.getByCpIdAndStage(cpId, stage)
        if (periodEntity.owner != owner) throw ErrorException(INVALID_OWNER)
//        if (entity.isAnswered) throw ErrorException(ALREADY_HAS_ANSWER)
        val enquiryDto = dto.enquiry
        val enquiry = toObject(Enquiry::class.java, entity.jsonData)
        if (enquiryId != enquiry.id) throw ErrorException(INVALID_ID)
        if (enquiryDto.answer.isBlank()) throw ErrorException(INVALID_ANSWER)
        enquiry.apply {
            date = dateTime
            answer = enquiryDto.answer
            dateAnswered = dateTime
        }
        entity.apply {
            isAnswered = true
            jsonData = toJson(enquiry)
        }
        enquiryDao.save(entity)

        return ResponseDto(data = AddAnswerRs(enquiry))
    }

    override fun checkAnswer(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val token = cm.context.token ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)

        val entities = enquiryDao.findAllByCpIdAndStage(cpId, stage)
        val isAllAnswered = !entities.any{(it.token != UUID.fromString(token) && !it.isAnswered)}
        val periodEntity = periodDao.getByCpIdAndStage(cpId, stage)
        val endDate = periodEntity.endDate.toLocal()
        val isEnquiryPeriodExpired = (dateTime >= endDate)
        val setUnsuspended = isEnquiryPeriodExpired && isAllAnswered
        return ResponseDto(data = CheckAnswerRs(setUnsuspended))
    }

    private fun converterOrganizationReferenceCreateToOrganizationReference(author: OrganizationReferenceCreate): OrganizationReference {
        return OrganizationReference(
                name = author.name,
                id = author.identifier.scheme + "-" + author.identifier.id,
                identifier = converterIdentifierCreateToIdentifier(author.identifier),
                address = author.address,
                additionalIdentifiers = author.additionalIdentifiers,
                contactPoint = author.contactPoint,
                details = author.details
        )
    }

    private fun converterIdentifierCreateToIdentifier(identifier: IdentifierCreate): Identifier {
        return Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri.orEmpty())
    }

    private fun checkIsAllAnswered(cpId: String, stage: String): Boolean {
        val isAllAnswered = enquiryDao.getCountOfUnanswered(cpId, stage)
        return isAllAnswered == 0L
    }

    private fun getEntity(cpId: String,
                          token: UUID,
                          stage: String,
                          isAnswered: Boolean,
                          enquiry: Enquiry): EnquiryEntity {
        return EnquiryEntity(
                cpId = cpId,
                token = token,
                stage = stage,
                jsonData = toJson(enquiry),
                isAnswered = isAnswered
        )
    }

}
