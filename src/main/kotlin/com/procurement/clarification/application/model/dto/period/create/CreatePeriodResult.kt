package com.procurement.clarification.application.model.dto.period.create

import java.time.LocalDateTime

data class CreatePeriodResult(
    val enquiryPeriod: Period
) {
    data class Period(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime
    )
}
