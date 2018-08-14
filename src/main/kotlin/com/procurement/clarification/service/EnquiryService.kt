package com.procurement.clarification.service

import com.procurement.clarification.dao.EnquiryDao
import com.procurement.clarification.dao.PeriodDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.model.dto.CheckEnquiresResponseDto
import com.procurement.clarification.model.dto.UpdateEnquiryResponseDto
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.model.dto.ocds.Identifier
import com.procurement.clarification.model.dto.ocds.OrganizationReference
import com.procurement.clarification.model.dto.params.CreateEnquiryParams
import com.procurement.clarification.model.dto.params.UpdateEnquiryParams
import com.procurement.clarification.model.dto.request.IdentifierCreate
import com.procurement.clarification.model.dto.request.OrganizationReferenceCreate
import com.procurement.clarification.model.dto.response.CreateEnquiryResponseDto
import com.procurement.clarification.model.entity.EnquiryEntity
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.toLocal
import com.procurement.clarification.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface EnquiryService {

    fun createEnquiry(params: CreateEnquiryParams): ResponseDto

    fun createAnswer(params: UpdateEnquiryParams): ResponseDto

    fun checkEnquiries(cpId: String, stage: String, dateTime: LocalDateTime): ResponseDto
}

@Service
class EnquiryServiceImpl(private val generationService: GenerationService,
                         private val enquiryDao: EnquiryDao,
                         private val periodDao: PeriodDao,
                         private val periodService: PeriodService) : EnquiryService {

    override fun createEnquiry(params: CreateEnquiryParams): ResponseDto {
        periodService.checkDateInPeriod(params.dateTime, params.cpId, params.stage)
        val enquiryRequest = params.data.enquiry
        val periodEntity = periodDao.getByCpIdAndStage(params.cpId, params.stage)
        val owner = periodEntity.owner
        val author = converterOrganizationReferenceCreateToOrganizationReference(enquiryRequest.author)
        val enquiry = Enquiry(
                id = generationService.generateTimeBasedUUID().toString(),
                date = params.dateTime,
                author = author,
                title = enquiryRequest.title,
                description = enquiryRequest.description,
                answer = null,
                relatedItem = enquiryRequest.relatedItem,
                relatedLot = enquiryRequest.relatedLot,
                dateAnswered = null
        )

        val entity = getEntity(
                cpId = params.cpId,
                token = generationService.generateRandomUUID(),
                stage = params.stage,
                isAnswered = false,
                enquiry = enquiry
        )
        enquiryDao.save(entity)
        return ResponseDto(true, null,
                CreateEnquiryResponseDto(entity.token_entity.toString(), owner, enquiry))
    }

    override fun createAnswer(params: UpdateEnquiryParams): ResponseDto {
        val entity = enquiryDao.getByCpIdAndStageAndToken(params.cpId, params.stage, UUID.fromString(params.token))
        val periodEntity = periodDao.getByCpIdAndStage(params.cpId, params.stage)
        if (periodEntity.owner != params.owner) throw ErrorException(ErrorType.INVALID_OWNER)
        if (entity.isAnswered) throw ErrorException(ErrorType.ALREADY_HAS_ANSWER)
        val enquiryDto = params.data.enquiry
        val enquiry = toObject(Enquiry::class.java, entity.jsonData)
        if (params.enquiryId != enquiry.id) throw ErrorException(ErrorType.INVALID_ID)
        if (enquiryDto.answer.isBlank()) throw ErrorException(ErrorType.INVALID_ANSWER)
        enquiry.apply {
            date = params.dateTime
            answer = enquiryDto.answer
            dateAnswered = params.dateTime
        }
        val newEntity = getEntity(
                cpId = entity.cpId,
                token = entity.token_entity,
                stage = entity.stage,
                isAnswered = true,
                enquiry = enquiry
        )
        enquiryDao.save(newEntity)
        val allAnswered = checkIsAllAnsweredAfterEndPeriod(params.cpId, params.stage, params.dateTime)
        return ResponseDto(true, null, UpdateEnquiryResponseDto(allAnswered, enquiry))
    }

    override fun checkEnquiries(cpId: String, stage: String, dateTime: LocalDateTime): ResponseDto {
        val tenderPeriodEndDate = periodService.getPeriodEntity(cpId, stage).tenderEndDate.toLocal()
        val isTenderPeriodExpired = (dateTime >= tenderPeriodEndDate)
        val isAllAnswered = checkIsAllAnswered(cpId, stage)
        return ResponseDto(true, null,
                CheckEnquiresResponseDto(
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
