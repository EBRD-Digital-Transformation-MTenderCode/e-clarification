package com.procurement.clarification.service

import com.procurement.clarification.dao.PeriodDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.ocds.Period
import com.procurement.clarification.model.dto.params.PeriodParams
import com.procurement.clarification.model.entity.PeriodEntity
import com.procurement.clarification.utils.toDate
import com.procurement.clarification.utils.toLocal
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface PeriodService {

    fun calculateAndSavePeriod(params: PeriodParams): ResponseDto

    fun getPeriod(cpId: String, stage: String): ResponseDto

    fun checkDateInPeriod(localDateTime: LocalDateTime, cpId: String, stage: String)

    fun getPeriodEntity(cpId: String, stage: String): PeriodEntity
}

@Service
class PeriodServiceImpl(private val periodDao: PeriodDao,
                        private val rulesService: RulesService) : PeriodService {

    override fun calculateAndSavePeriod(params: PeriodParams): ResponseDto {

        val offset = if (params.setExtendedPeriod) {
            rulesService.getOffsetExtended(params.country, params.pmd)
        } else {
            rulesService.getOffset(params.country, params.pmd)
        }
        val enquiryEndDate = params.endDate.minusSeconds(offset)
        val periodEntity = getEntity(
                cpId = params.cpId,
                stage = params.stage,
                startDate = params.startDate.toDate(),
                endDate = enquiryEndDate.toDate(),
                tenderEndDate = params.endDate.toDate()
        )
        periodDao.save(periodEntity)
        return ResponseDto(true, null,
                Period(periodEntity.startDate.toLocal(), periodEntity.endDate.toLocal()))
    }

    override fun getPeriod(cpId: String, stage: String): ResponseDto {
        val entity = getPeriodEntity(cpId, stage)
        return ResponseDto(true, null, Period(entity.startDate.toLocal(), entity.endDate.toLocal()))
    }

    override fun checkDateInPeriod(localDateTime: LocalDateTime,
                                   cpId: String,
                                   stage: String) {
        val periodEntity = getPeriodEntity(cpId, stage)
        val localDateTimeAfter = localDateTime.isAfter(periodEntity.startDate.toLocal())
                || localDateTime == periodEntity.startDate.toLocal()
        val localDateTimeBefore = localDateTime.isBefore(periodEntity.endDate.toLocal())
                || localDateTime == periodEntity.endDate.toLocal()
        if (!localDateTimeBefore || !localDateTimeAfter) throw ErrorException(ErrorType.INVALID_DATE)
    }

    override fun getPeriodEntity(cpId: String, stage: String): PeriodEntity {
        return periodDao.getByCpIdAndStage(cpId, stage)
    }

    private fun getEntity(cpId: String,
                          stage: String,
                          startDate: Date,
                          endDate: Date,
                          tenderEndDate: Date): PeriodEntity {
        return PeriodEntity(
                cpId = cpId,
                stage = stage,
                startDate = startDate,
                endDate = endDate,
                tenderEndDate = tenderEndDate
        )
    }
}
