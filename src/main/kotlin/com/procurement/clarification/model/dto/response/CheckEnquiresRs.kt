package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CheckEnquiresRs(

        @get:JsonProperty("isTenderPeriodExpired")
        val isTenderPeriodExpired: Boolean?,

        val tenderPeriodEndDate: LocalDateTime?,

        @get: JsonProperty("allAnswered")
        val allAnswered: Boolean
)
