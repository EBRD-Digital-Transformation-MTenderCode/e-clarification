package com.procurement.clarification.infrastructure.handler.v1.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.clarification.model.dto.ocds.Enquiry


@JsonInclude(JsonInclude.Include.NON_NULL)
data class AddAnswerRs(

        val enquiry: Enquiry
)
