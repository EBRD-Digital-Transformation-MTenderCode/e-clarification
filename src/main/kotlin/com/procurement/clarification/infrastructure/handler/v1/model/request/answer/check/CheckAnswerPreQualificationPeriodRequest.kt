package com.procurement.clarification.infrastructure.handler.v1.model.request.answer.check


import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CheckAnswerPreQualificationPeriodRequest(
    @param:JsonProperty("preQualification") @field:JsonProperty("preQualification") val preQualification: PreQualification
) {
    data class PreQualification(
        @param:JsonProperty("period") @field:JsonProperty("period") val period: Period
    ) {
        data class Period(
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: LocalDateTime
        )
    }
}