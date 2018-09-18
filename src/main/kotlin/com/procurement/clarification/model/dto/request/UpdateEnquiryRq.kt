package com.procurement.clarification.model.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid

data class UpdateEnquiryRq @JsonCreator constructor(

        @field:Valid
        val enquiry: EnquiryAnswer
)

data class EnquiryAnswer @JsonCreator constructor(

        val answer: String
)
