package com.procurement.clarification.infrastructure.handler.v1.model.request


import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CreatePeriodRequest(
    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period
) {
    data class Period(
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
    )
}
