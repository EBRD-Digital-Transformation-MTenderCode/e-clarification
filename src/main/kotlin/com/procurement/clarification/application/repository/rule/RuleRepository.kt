package com.procurement.clarification.application.repository.rule

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.enums.OperationType
import com.procurement.clarification.domain.model.enums.ProcurementMethod
import com.procurement.clarification.lib.functional.Result

interface RuleRepository {
    fun find(
        country: String,
        pmd: ProcurementMethod,
        parameter: String,
        operationType: OperationType? = null
    ): Result<String?, Fail.Incident.Database.Interaction>
}
