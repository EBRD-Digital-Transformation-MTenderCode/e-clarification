package com.procurement.clarification.service

import com.procurement.clarification.dao.PeriodDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.ocds.Period
import com.procurement.clarification.model.entity.PeriodEntity
import com.procurement.clarification.utils.toDate
import com.procurement.clarification.utils.toLocal
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface PeriodService {

    fun calculateAndSavePeriod(cm: CommandMessage): ResponseDto

    fun getPeriod(cm: CommandMessage): ResponseDto

    fun checkDateInPeriod(localDateTime: LocalDateTime, cpId: String, stage: String)

    fun getPeriodEntity(cpId: String, stage: String): PeriodEntity
}

@Service
class PeriodServiceImpl(private val periodDao: PeriodDao,
                        private val rulesService: RulesService) : PeriodService {

    override fun calculateAndSavePeriod(cm: CommandMessage): ResponseDto {
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
        val enquiryEndDate = endDate.minusSeconds(offset)
        val periodEntity = getEntity(
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

    override fun getPeriod(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(ErrorType.CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(ErrorType.CONTEXT)
        val entity = getPeriodEntity(cpId, stage)
        return ResponseDto(data = Period(entity.startDate.toLocal(), entity.endDate.toLocal()))
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
                          owner: String,
                          startDate: Date,
                          endDate: Date,
                          tenderEndDate: Date): PeriodEntity {
        return PeriodEntity(
                cpId = cpId,
                stage = stage,
                owner = owner,
                startDate = startDate,
                endDate = endDate,
                tenderEndDate = tenderEndDate
        )
    }
}
