package com.procurement.clarification.infrastructure.handler.find.enquiries.period

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CreateEnquiryPeriodResult(
    @param:JsonProperty("tender") @field:JsonProperty("tender") val tender: Tender
) {
    data class Tender(
        @param:JsonProperty("enquiryPeriod") @field:JsonProperty("enquiryPeriod") val enquiryPeriod: EnquiryPeriod
    ) {
        data class EnquiryPeriod(
            @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: LocalDateTime,
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: LocalDateTime
        )
    }
}