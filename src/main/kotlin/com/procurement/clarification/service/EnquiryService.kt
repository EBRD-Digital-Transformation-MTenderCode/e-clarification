package com.procurement.clarification.service

import com.procurement.clarification.dao.EnquiryDao
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType.ALREADY_HAS_ANSWER
import com.procurement.clarification.exception.ErrorType.CONTEXT
import com.procurement.clarification.exception.ErrorType.INVALID_ANSWER
import com.procurement.clarification.exception.ErrorType.INVALID_ID
import com.procurement.clarification.exception.ErrorType.INVALID_OWNER
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.bpe.cpid
import com.procurement.clarification.model.dto.bpe.ctxId
import com.procurement.clarification.model.dto.bpe.ocid
import com.procurement.clarification.model.dto.bpe.owner
import com.procurement.clarification.model.dto.bpe.startDate
import com.procurement.clarification.model.dto.bpe.token
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.model.dto.ocds.OrganizationReference
import com.procurement.clarification.model.dto.ocds.Period
import com.procurement.clarification.model.dto.ocds.Tender
import com.procurement.clarification.model.dto.request.AddAnswerRq
import com.procurement.clarification.model.dto.request.CreateEnquiryRq
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

@Service
class EnquiryService(
    private val generationService: GenerationService,
    private val enquiryDao: EnquiryDao,
    private val periodService: PeriodService
) {

    fun createEnquiry(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val dateTime = cm.startDate
        val dto = toObject(CreateEnquiryRq::class.java, cm.data)

        periodService.checkDateInPeriod(dateTime, cpid, ocid)
        val periodEntity = periodService.getPeriodEntity(cpid, ocid)

        val enquiryRequest = dto.enquiry
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
            cpId = cpid.underlying,
            token = generationService.generateRandomUUID(),
            stage = ocid.stage.key,
            isAnswered = false,
            enquiry = enquiry
        )
        enquiryDao.save(entity)
        return ResponseDto(data = CreateEnquiryRs(entity.token.toString(), owner, enquiry))
    }

    fun checkEnquiries(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val dateTime = cm.startDate
        val enquiryPeriod = periodService.getPeriodEntity(cpid, ocid)
        val startDate = enquiryPeriod.startDate
        val endDate = enquiryPeriod.endDate
        val isEnquiryPeriodExpired = (dateTime >= endDate)
        val isAllAnswered = checkIsAllAnswered(cpid, ocid)
        return ResponseDto(
            data = CheckEnquiresRs(
                isEnquiryPeriodExpired = isEnquiryPeriodExpired,
                tender = Tender(Period(startDate, endDate)),
                allAnswered = isAllAnswered
            )
        )
    }

    fun addAnswer(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val token = cm.token
        val owner = cm.owner
        val enquiryId = cm.ctxId
        val dateTime = cm.startDate
        val dto = toObject(AddAnswerRq::class.java, cm.data)

        val entity = enquiryDao.getByCpIdAndStageAndToken(cpid.underlying, ocid.stage.key, token)
        val periodEntity = periodService.getPeriodEntity(cpid, ocid)
        if (periodEntity.owner != owner) throw ErrorException(INVALID_OWNER)
        if (entity.isAnswered) throw ErrorException(ALREADY_HAS_ANSWER)
        val enquiryDto = dto.enquiry
        val enquiry = toObject(Enquiry::class.java, entity.jsonData)
        if (enquiryId != enquiry.id) throw ErrorException(INVALID_ID)
        if (enquiryDto.answer.isBlank()) throw ErrorException(INVALID_ANSWER)
        enquiry.apply {
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

    fun checkAnswer(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val token = cm.token
        val owner = cm.owner
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)
        toObject(AddAnswerRq::class.java, cm.data)
        val entities = enquiryDao.findAllByCpIdAndStage(cpid.underlying, ocid.stage.key)
        val isAllAnswered = !entities.any { (it.token != token && !it.isAnswered) }
        val periodEntity = periodService.getPeriodEntity(cpid, ocid)
        if (periodEntity.owner != owner) throw ErrorException(INVALID_OWNER)
        val endDate = periodEntity.endDate
        val isEnquiryPeriodExpired = (dateTime >= endDate)
        val setUnsuspended = isEnquiryPeriodExpired && isAllAnswered
        return ResponseDto(data = CheckAnswerRs(setUnsuspended))
    }

    private fun converterOrganizationReferenceCreateToOrganizationReference(author: OrganizationReferenceCreate): OrganizationReference {
        return OrganizationReference(
            name = author.name,
            id = author.identifier.scheme + "-" + author.identifier.id,
            identifier = author.identifier,
            address = author.address,
            additionalIdentifiers = author.additionalIdentifiers,
            contactPoint = author.contactPoint,
            details = author.details
        )
    }

    private fun checkIsAllAnswered(cpid: Cpid, ocid: Ocid): Boolean {
        val isAllAnswered = enquiryDao.getCountOfUnanswered(cpid.underlying, ocid.stage.key)
        return isAllAnswered == 0L
    }

    private fun getEntity(
        cpId: String,
        token: UUID,
        stage: String,
        isAnswered: Boolean,
        enquiry: Enquiry
    ): EnquiryEntity {
        return EnquiryEntity(
            cpId = cpId,
            token = token,
            stage = stage,
            jsonData = toJson(enquiry),
            isAnswered = isAnswered
        )
    }
}
