package com.procurement.clarification.infrastructure.handler.v1.model.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CreatePeriodResponse(
    @field:JsonProperty("enquiryPeriod") @param:JsonProperty("enquiryPeriod") val enquiryPeriod: Period
) {
    data class Period(
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
    )
}
