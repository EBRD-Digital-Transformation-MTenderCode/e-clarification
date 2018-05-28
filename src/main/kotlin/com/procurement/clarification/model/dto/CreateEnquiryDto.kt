package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.model.dto.ocds.Enquiry
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class CreateEnquiryDto(

        @JsonProperty("enquiry") @Valid @NotNull
        val enquiry: Enquiry
)
