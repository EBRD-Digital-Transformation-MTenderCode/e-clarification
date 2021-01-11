package com.procurement.clarification.infrastructure.handler.v1.model.request.answer.check


import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CheckAnswerTenderPeriodRequest(
    @param:JsonProperty("tender") @field:JsonProperty("tender") val tender: Tender
) {
    data class Tender(
        @param:JsonProperty("tenderPeriod") @field:JsonProperty("tenderPeriod") val tenderPeriod: TenderPeriod
    ) {
        data class TenderPeriod(
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: LocalDateTime
        )
    }
}