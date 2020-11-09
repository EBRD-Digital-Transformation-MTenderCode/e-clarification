package com.procurement.clarification.application.model.dto.period.create

import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.enums.ProcurementMethod

data class CreatePeriodContext(
    val cpid: Cpid,
    val ocid: Ocid,
    val owner: String,
    val country: String,
    val pmd: ProcurementMethod
)
