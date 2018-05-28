package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.model.dto.ocds.Enquiry
import javax.validation.constraints.NotNull


@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateEnquiryResponseDto(

        @JsonProperty("allAnswered")
        @get:JsonProperty("allAnswered")
        val allAnswered: Boolean?,

        @JsonProperty("enquiry") @NotNull
        val enquiry: Enquiry
)
