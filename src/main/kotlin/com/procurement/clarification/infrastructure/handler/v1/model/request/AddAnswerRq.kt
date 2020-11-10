package com.procurement.clarification.infrastructure.handler.v1.model.request

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid

data class AddAnswerRq @JsonCreator constructor(

        @field:Valid
        val enquiry: EnquiryAnswer
)

data class EnquiryAnswer @JsonCreator constructor(

        val answer: String
)