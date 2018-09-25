package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.model.dto.ocds.Enquiry


@JsonInclude(JsonInclude.Include.NON_NULL)
data class AddAnswerRs(

        @get:JsonProperty("setUnsuspended")
        val setUnsuspended: Boolean,

        val enquiry: Enquiry
)
