package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonProperty


data class EnquiryAnswerDto(

        @JsonProperty("id")
        val id: String,

        @JsonProperty("answer")
        val answer: String
)
