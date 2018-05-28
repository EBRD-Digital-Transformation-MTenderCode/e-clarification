package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.clarification.model.dto.databinding.JsonDateDeserializer
import com.procurement.clarification.model.dto.databinding.JsonDateSerializer
import java.time.LocalDateTime


data class CheckEnquiresResponseDto(

        @JsonProperty("allAnswered")
        @get: JsonProperty("allAnswered")
        val allAnswered: Boolean?,

        @JsonProperty("tenderPeriodEndDate")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val tenderPeriodEndDate: LocalDateTime?
)
