package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonCreator


data class EnquiryAnswerDto @JsonCreator constructor(

        val id: String,

        val answer: String
)
