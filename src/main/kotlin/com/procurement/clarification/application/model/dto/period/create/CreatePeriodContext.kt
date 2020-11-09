package com.procurement.clarification.application.model.dto.period.create

import com.procurement.clarification.domain.model.enums.ProcurementMethod

data class CreatePeriodContext(
    val owner: String,
    val cpid: String,
    val country: String,
    val pmd: ProcurementMethod,
    val stage: String
)
