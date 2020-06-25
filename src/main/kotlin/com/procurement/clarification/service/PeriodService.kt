package com.procurement.clarification.service

import com.procurement.clarification.application.model.dto.period.create.CreatePeriodContext
import com.procurement.clarification.application.model.dto.period.create.CreatePeriodData
import com.procurement.clarification.application.model.dto.period.create.CreatePeriodResult
import com.procurement.clarification.dao.PeriodDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.ocds.Period
import com.procurement.clarification.model.dto.request.PeriodRq
import com.procurement.clarification.model.dto.response.CheckPeriodRs
import com.procurement.clarification.model.entity.PeriodEntity
import com.procurement.clarification.utils.toDate
import com.procurement.clarification.utils.toLocal
import com.procurement.clarification.utils.toObject
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class PeriodService(private val periodDao: PeriodDao,
                    private val rulesService: RulesService) {

    fun periodValidation(cm: CommandMessage): ResponseDto {
        val country = cm.context.country ?: throw ErrorException(ErrorType.CONTEXT)
        val pmd = cm.context.pmd ?: throw ErrorException(ErrorType.CONTEXT)
        val enquiryPeriod = toObject(PeriodRq::class.java, cm.data).enquiryPeriod
        val startDate = enquiryPeriod.startDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)
        val endDate = enquiryPeriod.endDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)

        if (!checkInterval(country, pmd, startDate, endDate)) throw ErrorException(ErrorType.INVALID_PERIOD)
        return ResponseDto(data = "Period is valid.")
    }

    fun savePeriod(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(ErrorType.CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(ErrorType.CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(ErrorType.CONTEXT)
        val enquiryPeriod = toObject(PeriodRq::class.java, cm.data).enquiryPeriod
        val startDate = enquiryPeriod.startDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)
        val endDate = enquiryPeriod.endDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)

        val period = PeriodEntity(
                cpId = cpId,
                stage = stage,
                owner = owner,
                startDate = startDate.toDate(),
                endDate = endDate.toDate(),
                tenderEndDate = null)
        periodDao.save(period)
        return ResponseDto(data = Period(period.startDate.toLocal(), period.endDate.toLocal()))
    }

    fun saveNewPeriod(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(ErrorType.CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(ErrorType.CONTEXT)
        val country = cm.context.country ?: throw ErrorException(ErrorType.CONTEXT)
        val pmd = cm.context.pmd ?: throw ErrorException(ErrorType.CONTEXT)
        val startDateContext = cm.context.startDate?.toLocal() ?: throw ErrorException(ErrorType.INVALID_PERIOD)
        val offsetExtended = rulesService.getOffsetExtended(country, pmd)
        val newEndDate = startDateContext.plus(offsetExtended)
        val oldPeriod = getPeriodEntity(cpId, stage)
        val newPeriod = oldPeriod.copy(endDate = newEndDate.toDate())
        periodDao.save(newPeriod)
        return ResponseDto(data = Period(newPeriod.startDate.toLocal(), newPeriod.endDate.toLocal()))
    }

    fun calculateAndSavePeriod(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(ErrorType.CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(ErrorType.CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(ErrorType.CONTEXT)
        val country = cm.context.country ?: throw ErrorException(ErrorType.CONTEXT)
        val pmd = cm.context.pmd ?: throw ErrorException(ErrorType.CONTEXT)
        val startDate = cm.context.startDate?.toLocal() ?: throw ErrorException(ErrorType.CONTEXT)
        val endDate = cm.context.endDate?.toLocal() ?: throw ErrorException(ErrorType.CONTEXT)
        val setExtendedPeriod = cm.context.setExtendedPeriod ?: throw ErrorException(ErrorType.CONTEXT)

        val offset = if (setExtendedPeriod) {
            rulesService.getOffsetExtended(country, pmd)
        } else {
            rulesService.getOffset(country, pmd)
        }
        val enquiryEndDate = endDate.minus(offset)
        val periodEntity = PeriodEntity(
                cpId = cpId,
                stage = stage,
                owner = owner,
                startDate = startDate.toDate(),
                endDate = enquiryEndDate.toDate(),
                tenderEndDate = endDate.toDate()
        )
        periodDao.save(periodEntity)
        return ResponseDto(data = Period(periodEntity.startDate.toLocal(), periodEntity.endDate.toLocal()))
    }

    fun getPeriod(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(ErrorType.CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(ErrorType.CONTEXT)

        val entity = getPeriodEntity(cpId, stage)
        return ResponseDto(data = Period(entity.startDate.toLocal(), entity.endDate.toLocal()))
    }

    fun createPeriod(context: CreatePeriodContext, request: CreatePeriodData): CreatePeriodResult {

        // FR.COM-8.1.1
        val startDate = request.period.startDate

        // FR.COM-8.1.2
        val shift = rulesService.getPeriodShift(country = context.country, pmd = context.pmd)
        val endDate = request.period.endDate.minus(shift)

        val periodEntity = PeriodEntity(
            cpId = context.cpid,
            owner = context.owner,
            stage = context.stage,
            startDate = startDate.toDate(),
            endDate = endDate.toDate(),
            tenderEndDate = null
        )

        val result = CreatePeriodResult(
            enquiryPeriod = CreatePeriodResult.Period(
                startDate = periodEntity.startDate.toLocal(),
                endDate = periodEntity.endDate.toLocal()
            )
        )

        // FR.COM-8.1.3
        // FR.COM-8.1.4
        periodDao.save(periodEntity)

        // FR.COM-8.1.5
        return result
    }


    fun checkPeriod(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(ErrorType.CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(ErrorType.CONTEXT)
        val country = cm.context.country ?: throw ErrorException(ErrorType.CONTEXT)
        val pmd = cm.context.pmd ?: throw ErrorException(ErrorType.CONTEXT)
        val dto = toObject(PeriodRq::class.java, cm.data)
        val startDateRq = dto.enquiryPeriod.startDate ?: throw ErrorException(ErrorType.INVALID_PERIOD) // from payload
        val endDateRq = dto.enquiryPeriod.endDate ?: throw ErrorException(ErrorType.INVALID_PERIOD)// from payload

        val intervalBefore = rulesService.getIntervalBefore(country, pmd)
        val periodEntity = getPeriodEntity(cpId, stage)
        val startDateDb = periodEntity.startDate.toLocal()
        val endDateDb = periodEntity.endDate.toLocal()
        val checkPoint = endDateDb.minus(intervalBefore)
        //((pmd == "OT" && stage == "EV")
        if (endDateRq < endDateDb) throw ErrorException(ErrorType.INVALID_PERIOD)
        //a)
        if (startDateRq < checkPoint) {
            if (endDateRq == endDateDb) {
                return ResponseDto(data = CheckPeriodRs(
                        setExtendedPeriod = false,
                        isEnquiryPeriodChanged = false,
                        startDate = startDateDb,
                        endDate = endDateDb))
            }
            if (endDateDb < endDateRq) {
                return ResponseDto(data = CheckPeriodRs(
                        setExtendedPeriod = false,
                        isEnquiryPeriodChanged = true,
                        startDate = startDateDb,
                        endDate = endDateRq))
            }
        }
        //b)
        if (startDateRq >= checkPoint) {
            if (endDateRq == endDateDb) {
                val newEndDate = startDateRq.plus(intervalBefore)
                return ResponseDto(data = CheckPeriodRs(
                        setExtendedPeriod = true,
                        isEnquiryPeriodChanged = true,
                        startDate = startDateDb,
                        endDate = newEndDate))
            }
            if (endDateDb < endDateRq) {
                val newEndDate = startDateRq.plus(intervalBefore)
                if (endDateRq <= newEndDate) throw ErrorException(ErrorType.INVALID_PERIOD)
                return ResponseDto(data = CheckPeriodRs(
                        setExtendedPeriod = true,
                        isEnquiryPeriodChanged = true,
                        startDate = startDateDb,
                        endDate = endDateRq))
            }
        }
        return ResponseDto(null)
    }

    fun checkDateInPeriod(localDateTime: LocalDateTime,
                          cpId: String,
                          stage: String) {
        val periodEntity = getPeriodEntity(cpId, stage)
        val localDateTimeAfter = localDateTime.isAfter(periodEntity.startDate.toLocal())
                || localDateTime == periodEntity.startDate.toLocal()
        val localDateTimeBefore = localDateTime.isBefore(periodEntity.endDate.toLocal())
                || localDateTime == periodEntity.endDate.toLocal()
        if (!localDateTimeBefore || !localDateTimeAfter) throw ErrorException(ErrorType.INVALID_DATE)
    }

    fun getPeriodEntity(cpId: String, stage: String): PeriodEntity {
        return periodDao.getByCpIdAndStage(cpId, stage)
    }


    private fun checkInterval(country: String,
                              pmd: String,
                              startDate: LocalDateTime,
                              endDate: LocalDateTime): Boolean {
        val interval = rulesService.getInterval(country, pmd)
        val duration = Duration.between(startDate, endDate)
        return duration >= interval
    }

}
