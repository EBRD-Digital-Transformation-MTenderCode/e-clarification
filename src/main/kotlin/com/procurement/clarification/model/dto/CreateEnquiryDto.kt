package com.procurement.clarification.model.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.ocds.Enquiry
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class CreateEnquiryDto @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val enquiry: Enquiry
)
