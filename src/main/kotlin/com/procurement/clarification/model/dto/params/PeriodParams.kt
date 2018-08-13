package com.procurement.clarification.model.dto.params

import java.time.LocalDateTime

data class PeriodParams(
        val cpId: String,
        val stage: String,
        val owner: String,
        val country: String,
        val pmd: String,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val setExtendedPeriod: Boolean
)
