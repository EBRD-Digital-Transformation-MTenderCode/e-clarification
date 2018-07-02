package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class UpdateEnquiryDto @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val enquiry: EnquiryAnswerDto
)
