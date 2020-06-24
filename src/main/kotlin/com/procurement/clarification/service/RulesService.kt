package com.procurement.clarification.service

import com.procurement.clarification.dao.RulesDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RulesService(private val rulesDao: RulesDao) {

    fun getInterval(country: String, pmd: String): Duration {
        return rulesDao.getValue(country, pmd, PARAMETER_INTERVAL)
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.INTERVAL_RULES_NOT_FOUND)
    }

    fun getOffset(country: String, pmd: String): Duration {
        return rulesDao.getValue(country, pmd, PARAMETER_OFFSET)
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.OFFSET_RULES_NOT_FOUND)
    }

    fun getOffsetExtended(country: String, pmd: String): Duration {
        return rulesDao.getValue(country, pmd, PARAMETER_OFFSET_EXTENDED)
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.OFFSET_RULES_NOT_FOUND)
    }

    fun getIntervalBefore(country: String, pmd: String): Duration {
        return rulesDao.getValue(country, pmd, PARAMETER_INTERVAL_BEFORE)
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.INTERVAL_RULES_NOT_FOUND)
    }

    fun getPeriodShift(country: String, pmd: String): Duration {
        return rulesDao.getValue(country, pmd, PARAMETER_PERIOD_SHIFT)
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.INTERVAL_RULES_NOT_FOUND)
    }

    companion object {
        private const val PARAMETER_INTERVAL = "interval"
        private const val PARAMETER_INTERVAL_BEFORE = "interval_before"
        private const val PARAMETER_OFFSET = "offset"
        private const val PARAMETER_OFFSET_EXTENDED = "offsetExtended"
        private const val PARAMETER_PERIOD_SHIFT = "period_shift"
    }
}
