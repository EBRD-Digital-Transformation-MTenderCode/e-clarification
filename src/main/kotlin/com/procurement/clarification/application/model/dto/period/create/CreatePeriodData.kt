package com.procurement.clarification.application.model.dto.period.create

import java.time.LocalDateTime

data class CreatePeriodData(
    val period: Period
) {
    data class Period(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime
    )
}
