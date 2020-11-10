package com.procurement.clarification.service

import com.procurement.clarification.application.model.dto.params.CreateEnquiryPeriodParams
import com.procurement.clarification.application.model.dto.period.create.CreatePeriodContext
import com.procurement.clarification.application.model.dto.period.create.CreatePeriodData
import com.procurement.clarification.application.model.dto.period.create.CreatePeriodResult
import com.procurement.clarification.application.repository.period.PeriodRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.enums.ProcurementMethod
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.infrastructure.handler.enquiry.period.create.CreateEnquiryPeriodResult
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asFailure
import com.procurement.clarification.lib.functional.asSuccess
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.bpe.country
import com.procurement.clarification.model.dto.bpe.cpid
import com.procurement.clarification.model.dto.bpe.ocid
import com.procurement.clarification.model.dto.bpe.owner
import com.procurement.clarification.model.dto.bpe.pmd
import com.procurement.clarification.model.dto.ocds.Period
import com.procurement.clarification.model.dto.request.PeriodRq
import com.procurement.clarification.model.dto.response.CheckPeriodRs
import com.procurement.clarification.application.repository.period.model.PeriodEntity
import com.procurement.clarification.utils.toLocal
import com.procurement.clarification.utils.toObject
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class PeriodService(
    private val periodRepository: PeriodRepository,
    private val rulesService: RulesService
) {

    fun periodValidation(cm: CommandMessage): ResponseDto {
        val country = cm.country
        val pmd = cm.pmd
        val enquiryPeriod = toObject(PeriodRq::class.java, cm.data).enquiryPeriod
        val startDate = enquiryPeriod.startDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)
        val endDate = enquiryPeriod.endDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)

        if (!checkInterval(country, pmd, startDate, endDate)) throw ErrorException(ErrorType.INVALID_PERIOD)
        return ResponseDto(data = "Period is valid.")
    }

    fun savePeriod(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val owner = cm.owner
        val enquiryPeriod = toObject(PeriodRq::class.java, cm.data).enquiryPeriod
        val startDate = enquiryPeriod.startDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)
        val endDate = enquiryPeriod.endDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)

        val period = PeriodEntity(
            cpid = cpid,
            ocid = ocid,
            owner = owner,
            startDate = startDate,
            endDate = endDate,
            tenderEndDate = null
        )
        periodRepository.save(period)
        return ResponseDto(data = Period(period.startDate, period.endDate))
    }

    fun saveNewPeriod(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val country = cm.country
        val pmd = cm.pmd
        val startDateContext = cm.context.startDate?.toLocal() ?: throw ErrorException(ErrorType.INVALID_PERIOD)
        val offsetExtended = rulesService.getOffsetExtended(country, pmd)
        val newEndDate = startDateContext.plus(offsetExtended)
        val oldPeriod = getPeriodEntity(cpid, ocid)
        val newPeriod = oldPeriod.copy(endDate = newEndDate)
        periodRepository.save(newPeriod)
        return ResponseDto(data = Period(newPeriod.startDate, newPeriod.endDate))
    }

    fun createPeriod(context: CreatePeriodContext, request: CreatePeriodData): CreatePeriodResult {

        // FR.COM-8.1.1
        val startDate = request.period.startDate

        // FR.COM-8.1.2
        val shift = rulesService.getPeriodShift(country = context.country, pmd = context.pmd)
        val endDate = request.period.endDate.minus(shift)

        val periodEntity = PeriodEntity(
            cpid = context.cpid,
            ocid = context.ocid,
            owner = context.owner,
            startDate = startDate,
            endDate = endDate,
            tenderEndDate = null
        )

        val result = CreatePeriodResult(
            enquiryPeriod = CreatePeriodResult.Period(
                startDate = periodEntity.startDate,
                endDate = periodEntity.endDate
            )
        )

        // FR.COM-8.1.3
        // FR.COM-8.1.4
        periodRepository.save(periodEntity)

        // FR.COM-8.1.5
        return result
    }

    fun checkPeriod(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val country = cm.country
        val pmd = cm.pmd
        val dto = toObject(PeriodRq::class.java, cm.data)
        val startDateRq = dto.enquiryPeriod.startDate ?: throw ErrorException(ErrorType.INVALID_PERIOD) // from payload
        val endDateRq = dto.enquiryPeriod.endDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)// from payload

        val intervalBefore = rulesService.getIntervalBefore(country, pmd)
        val periodEntity = getPeriodEntity(cpid, ocid)
        val startDateDb = periodEntity.startDate
        val endDateDb = periodEntity.endDate
        val checkPoint = endDateDb.minus(intervalBefore)
        //((pmd == "OT" && stage == "EV")
        if (endDateRq < endDateDb) throw ErrorException(ErrorType.INVALID_PERIOD)
        //a)
        if (startDateRq < checkPoint) {
            if (endDateRq == endDateDb) {
                return ResponseDto(
                    data = CheckPeriodRs(
                        setExtendedPeriod = false,
                        isEnquiryPeriodChanged = false,
                        startDate = startDateDb,
                        endDate = endDateDb
                    )
                )
            }
            if (endDateDb < endDateRq) {
                return ResponseDto(
                    data = CheckPeriodRs(
                        setExtendedPeriod = false,
                        isEnquiryPeriodChanged = true,
                        startDate = startDateDb,
                        endDate = endDateRq
                    )
                )
            }
        }
        //b)
        if (startDateRq >= checkPoint) {
            if (endDateRq == endDateDb) {
                val newEndDate = startDateRq.plus(intervalBefore)
                return ResponseDto(
                    data = CheckPeriodRs(
                        setExtendedPeriod = true,
                        isEnquiryPeriodChanged = true,
                        startDate = startDateDb,
                        endDate = newEndDate
                    )
                )
            }
            if (endDateDb < endDateRq) {
                val newEndDate = startDateRq.plus(intervalBefore)
                if (endDateRq <= newEndDate) throw ErrorException(ErrorType.INVALID_PERIOD)
                return ResponseDto(
                    data = CheckPeriodRs(
                        setExtendedPeriod = true,
                        isEnquiryPeriodChanged = true,
                        startDate = startDateDb,
                        endDate = endDateRq
                    )
                )
            }
        }
        return ResponseDto(null)
    }

    fun checkDateInPeriod(localDateTime: LocalDateTime, cpid: Cpid, ocid: Ocid) {
        val periodEntity = getPeriodEntity(cpid, ocid)
        val localDateTimeAfter = localDateTime.isAfter(periodEntity.startDate)
            || localDateTime == periodEntity.startDate
        val localDateTimeBefore = localDateTime.isBefore(periodEntity.endDate)
            || localDateTime == periodEntity.endDate
        if (!localDateTimeBefore || !localDateTimeAfter) throw ErrorException(ErrorType.INVALID_DATE)
    }

    fun getPeriodEntity(cpid: Cpid, ocid: Ocid): PeriodEntity = periodRepository.find(cpid, ocid)
        .onFailure { throw it.reason.exception }
        ?: throw ErrorException(ErrorType.PERIOD_NOT_FOUND)

    private fun checkInterval(
        country: String,
        pmd: ProcurementMethod,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Boolean {
        val interval = rulesService.getInterval(country, pmd)
        val duration = Duration.between(startDate, endDate)
        return duration >= interval
    }

    fun createEnquiryPeriod(params: CreateEnquiryPeriodParams): Result<CreateEnquiryPeriodResult, Fail> {
        val periodShift = rulesService.getPeriodShift(country = params.country, pmd = params.pmd).seconds
        val tenderPeriod = params.tender.tenderPeriod
        val enquiryPeriod = PeriodEntity(
            cpid = params.cpid,
            ocid = params.ocid,
            owner = params.owner.toString(),
            startDate = tenderPeriod.startDate,
            endDate = tenderPeriod.endDate.minusSeconds(periodShift),
            tenderEndDate = null
        )

        periodRepository.save(enquiryPeriod)
            .doOnFail { error -> return error.asFailure() }

        return CreateEnquiryPeriodResult(
            CreateEnquiryPeriodResult.Tender(
                CreateEnquiryPeriodResult.Tender.EnquiryPeriod(
                    startDate = enquiryPeriod.startDate,
                    endDate = enquiryPeriod.endDate
                )
            )
        ).asSuccess()
    }
}
