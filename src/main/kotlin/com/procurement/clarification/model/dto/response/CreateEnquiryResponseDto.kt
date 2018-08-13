package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.clarification.model.dto.ocds.Enquiry

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CreateEnquiryResponseDto(

        val token: String?,

        val ownerCA :String,

        val enquiry: Enquiry
)

