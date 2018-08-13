package com.procurement.clarification.model.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.request.EnquiryAnswerDto
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class UpdateEnquiryDto @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val enquiry: EnquiryAnswerDto
)
