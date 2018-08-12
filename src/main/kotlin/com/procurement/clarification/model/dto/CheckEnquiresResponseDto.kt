package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CheckEnquiresResponseDto(

        @get: JsonProperty("allAnswered")
        val allAnswered: Boolean
)
