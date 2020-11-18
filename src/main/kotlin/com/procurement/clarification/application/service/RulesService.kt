package com.procurement.clarification.application.service

import com.procurement.clarification.application.repository.rule.RuleRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.enums.ProcurementMethod
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asSuccess
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RulesService(private val ruleRepository: RuleRepository) {

    fun getInterval(country: String, pmd: ProcurementMethod): Duration {
        return ruleRepository.find(country, pmd, PARAMETER_INTERVAL)
            .onFailure { throw it.reason.exception }
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.INTERVAL_RULES_NOT_FOUND)
    }

    fun getOffset(country: String, pmd: ProcurementMethod): Duration {
        return ruleRepository.find(country, pmd, PARAMETER_OFFSET)
            .onFailure { throw it.reason.exception }
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.OFFSET_RULES_NOT_FOUND)
    }

    fun getOffsetExtended(country: String, pmd: ProcurementMethod): Duration {
        return ruleRepository.find(country, pmd, PARAMETER_OFFSET_EXTENDED)
            .onFailure { throw it.reason.exception }
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.OFFSET_RULES_NOT_FOUND)
    }

    fun getIntervalBefore(country: String, pmd: ProcurementMethod): Duration {
        return ruleRepository.find(country, pmd, PARAMETER_INTERVAL_BEFORE)
            .onFailure { throw it.reason.exception }
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            ?: throw ErrorException(ErrorType.INTERVAL_RULES_NOT_FOUND)
    }

//    fun getPeriodShift(country: String, pmd: ProcurementMethod): Duration {
//        return ruleRepository.find(country, pmd, PARAMETER_PERIOD_SHIFT)
//            .onFailure { throw it.reason.exception }
//            ?.toLongOrNull()
//            ?.let { Duration.ofSeconds(it) }
//            ?: throw ErrorException(ErrorType.INTERVAL_RULES_NOT_FOUND)
//    }

    fun getPeriodShift(country: String, pmd: ProcurementMethod): Result<Duration?, Fail.Incident.Database> =
        ruleRepository.find(country, pmd, PARAMETER_PERIOD_SHIFT)
            .onFailure { return it }
            ?.toLongOrNull()
            ?.let { Duration.ofSeconds(it) }
            .asSuccess()

    companion object {
        private const val PARAMETER_INTERVAL = "interval"
        private const val PARAMETER_INTERVAL_BEFORE = "interval_before"
        private const val PARAMETER_OFFSET = "offset"
        private const val PARAMETER_OFFSET_EXTENDED = "offsetExtended"
        private const val PARAMETER_PERIOD_SHIFT = "period_shift"
    }
}
