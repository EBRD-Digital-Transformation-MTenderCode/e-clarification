package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CheckEnquiresResponseDto(

        @get:JsonProperty("isTenderPeriodExpired")
        val isTenderPeriodExpired: Boolean?,

        val tenderPeriodEndDate: LocalDateTime?,

        @get: JsonProperty("allAnswered")
        val allAnswered: Boolean
)
