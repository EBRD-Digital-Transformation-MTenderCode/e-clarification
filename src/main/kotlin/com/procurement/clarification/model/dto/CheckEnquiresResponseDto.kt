package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckEnquiresResponseDto(

        @get: JsonProperty("allAnswered")
        val allAnswered: Boolean?,

        val tenderPeriodEndDate: LocalDateTime?
)
