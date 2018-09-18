package com.procurement.clarification.service

import com.procurement.clarification.dao.EnquiryDao
import com.procurement.clarification.dao.PeriodDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType.*
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.model.dto.ocds.Identifier
import com.procurement.clarification.model.dto.ocds.OrganizationReference
import com.procurement.clarification.model.dto.request.CreateEnquiryRq
import com.procurement.clarification.model.dto.request.IdentifierCreate
import com.procurement.clarification.model.dto.request.OrganizationReferenceCreate
import com.procurement.clarification.model.dto.request.UpdateEnquiryRq
import com.procurement.clarification.model.dto.response.CheckEnquiresRs
import com.procurement.clarification.model.dto.response.CreateEnquiryRs
import com.procurement.clarification.model.dto.response.UpdateEnquiryRs
import com.procurement.clarification.model.entity.EnquiryEntity
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.toLocal
import com.procurement.clarification.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface EnquiryService {

    fun createEnquiry(cm: CommandMessage): ResponseDto

    fun createAnswer(cm: CommandMessage): ResponseDto

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
        return ResponseDto(data = CreateEnquiryRs(entity.token_entity.toString(), owner, enquiry))
    }

    override fun createAnswer(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val token = cm.context.token ?: throw ErrorException(CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val enquiryId = cm.context.id ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)
        val dto = toObject(UpdateEnquiryRq::class.java, cm.data)


        val entity = enquiryDao.getByCpIdAndStageAndToken(cpId, stage, UUID.fromString(token))
        val periodEntity = periodDao.getByCpIdAndStage(cpId, stage)
        if (periodEntity.owner != owner) throw ErrorException(INVALID_OWNER)
        if (entity.isAnswered) throw ErrorException(ALREADY_HAS_ANSWER)
        val enquiryDto = dto.enquiry
        val enquiry = toObject(Enquiry::class.java, entity.jsonData)
        if (enquiryId != enquiry.id) throw ErrorException(INVALID_ID)
        if (enquiryDto.answer.isBlank()) throw ErrorException(INVALID_ANSWER)
        enquiry.apply {
            date = dateTime
            answer = enquiryDto.answer
            dateAnswered = dateTime
        }
        val newEntity = getEntity(
                cpId = entity.cpId,
                token = entity.token_entity,
                stage = entity.stage,
                isAnswered = true,
                enquiry = enquiry
        )
        enquiryDao.save(newEntity)
        val allAnswered = checkIsAllAnsweredAfterEndPeriod(cpId, stage, dateTime)
        return ResponseDto(data = UpdateEnquiryRs(allAnswered, enquiry))
    }

    override fun checkEnquiries(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)

        val tenderPeriodEndDate = periodService.getPeriodEntity(cpId, stage).tenderEndDate.toLocal()
        val isTenderPeriodExpired = (dateTime >= tenderPeriodEndDate)
        val isAllAnswered = checkIsAllAnswered(cpId, stage)
        return ResponseDto(data = CheckEnquiresRs(
                isTenderPeriodExpired = isTenderPeriodExpired,
                tenderPeriodEndDate = tenderPeriodEndDate,
                allAnswered = isAllAnswered))
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

    private fun checkIsAllAnsweredAfterEndPeriod(cpId: String, stage: String, dateTime: LocalDateTime): Boolean {
        val tenderEndDate = periodService.getPeriodEntity(cpId, stage).tenderEndDate
        return if (dateTime.isAfter(tenderEndDate.toLocal())) {
            checkIsAllAnswered(cpId, stage)
        } else {
            false
        }
    }

    private fun getEntity(cpId: String,
                          token: UUID,
                          stage: String,
                          isAnswered: Boolean,
                          enquiry: Enquiry): EnquiryEntity {
        return EnquiryEntity(
                cpId = cpId,
                token_entity = token,
                stage = stage,
                jsonData = toJson(enquiry),
                isAnswered = isAnswered
        )
    }

}
