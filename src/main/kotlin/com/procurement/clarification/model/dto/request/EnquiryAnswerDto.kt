package com.procurement.clarification.model.dto.request

import com.fasterxml.jackson.annotation.JsonCreator


data class EnquiryAnswerDto @JsonCreator constructor(

        val answer: String
)
