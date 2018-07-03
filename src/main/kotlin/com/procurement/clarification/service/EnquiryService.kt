package com.procurement.clarification.service

import com.procurement.clarification.dao.EnquiryDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.model.dto.CheckEnquiresResponseDto
import com.procurement.clarification.model.dto.CreateEnquiryResponseDto
import com.procurement.clarification.model.dto.UpdateEnquiryResponseDto
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.model.dto.params.CreateEnquiryParams
import com.procurement.clarification.model.dto.params.UpdateEnquiryParams
import com.procurement.clarification.model.entity.EnquiryEntity
import com.procurement.clarification.utils.localNowUTC
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.toLocal
import com.procurement.clarification.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface EnquiryService {

    fun createEnquiry(params: CreateEnquiryParams): ResponseDto

    fun createAnswer(params: UpdateEnquiryParams): ResponseDto

    fun checkEnquiries(cpId: String, stage: String): ResponseDto
}

@Service
class EnquiryServiceImpl(private val generationService: GenerationService,
                         private val enquiryDao: EnquiryDao,
                         private val periodService: PeriodService) : EnquiryService {

    override fun createEnquiry(params: CreateEnquiryParams): ResponseDto {
        periodService.checkDateInPeriod(params.dateTime, params.cpId, params.stage)
        val enquiry = params.data.enquiry
        enquiry.apply {
            id = generationService.generateTimeBasedUUID().toString()
            date = params.dateTime
            author.id = author.identifier.scheme + "-" + author.identifier.id
        }
        val entity = getEntity(
                cpId = params.cpId,
                token = generationService.generateRandomUUID(),
                stage = params.stage,
                owner = params.owner,
                isAnswered = false,
                enquiry = enquiry
        )
        enquiryDao.save(entity)
        return ResponseDto(true, null,
                CreateEnquiryResponseDto(entity.token.toString(), enquiry))
    }

    override fun createAnswer(params: UpdateEnquiryParams): ResponseDto {
        val entity = enquiryDao.getByCpIdAndStageAndToken(params.cpId, params.stage, UUID.fromString(params.token))
        if (entity.owner != params.owner) throw ErrorException(ErrorType.INVALID_OWNER)
        if (entity.isAnswered) throw ErrorException(ErrorType.ALREADY_HAS_ANSWER)
        val enquiryDto = params.data.enquiry
        val enquiry = toObject(Enquiry::class.java, entity.jsonData)
        if (enquiryDto.id != enquiry.id) throw ErrorException(ErrorType.INVALID_ID)
        enquiry.apply {
            date = params.dateTime
            answer = enquiryDto.answer
            dateAnswered = params.dateTime
        }
        val newEntity = getEntity(
                cpId = entity.cpId,
                token = entity.token,
                stage = entity.stage,
                owner = entity.owner,
                isAnswered = false,
                enquiry = enquiry
        )
        enquiryDao.save(newEntity)
        val allAnswered = checkIsAllAnsweredAfterEndPeriod(params.cpId, params.stage, params.dateTime)
        return ResponseDto(true, null, UpdateEnquiryResponseDto(allAnswered, enquiry))
    }

    override fun checkEnquiries(cpId: String, stage: String): ResponseDto {
        val tenderEndDate = periodService.getPeriod(cpId, stage).tenderEndDate
        return if (tenderEndDate.toLocal().isBefore(localNowUTC())) {
            val isAllAnswered = checkIsAllAnswered(cpId, stage)
            ResponseDto(true, null, CheckEnquiresResponseDto(isAllAnswered, null))
        } else {
            ResponseDto(true, null, CheckEnquiresResponseDto(null, tenderEndDate.toLocal()))
        }
    }

    private fun checkIsAllAnswered(cpId: String, stage: String): Boolean {
        val isAllAnswered = enquiryDao.getCountOfUnanswered(cpId, stage)
        return isAllAnswered == 0L
    }

    private fun checkIsAllAnsweredAfterEndPeriod(cpId: String, stage: String, dateTime: LocalDateTime): Boolean {
        val tenderEndDate = periodService.getPeriod(cpId, stage).tenderEndDate
        return if (dateTime.isAfter(tenderEndDate.toLocal())) {
            checkIsAllAnswered(cpId, stage)
        } else {
            false
        }
    }

    private fun getEntity(cpId: String,
                          token: UUID,
                          stage: String,
                          owner: String,
                          isAnswered: Boolean,
                          enquiry: Enquiry): EnquiryEntity {
        return EnquiryEntity(
                cpId = cpId,
                token = token,
                stage = stage,
                owner = owner,
                jsonData = toJson(enquiry),
                isAnswered = isAnswered
        )
    }

}
