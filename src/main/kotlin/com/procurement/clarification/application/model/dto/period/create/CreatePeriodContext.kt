package com.procurement.clarification.application.model.dto.period.create

data class CreatePeriodContext(
    val owner: String,
    val cpid: String,
    val country: String,
    val pmd: String,
    val stage: String
)
