package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class UpdateEnquiryDto(

        @JsonProperty("enquiry") @Valid @NotNull
        val enquiry: EnquiryAnswerDto
)
