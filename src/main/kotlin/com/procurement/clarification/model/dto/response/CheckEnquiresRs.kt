package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.model.dto.ocds.Tender

data class CheckEnquiresRs(

        @get:JsonProperty("isEnquiryPeriodExpired")
        val isEnquiryPeriodExpired: Boolean?,

        val tender: Tender?,

        @get: JsonProperty("allAnswered")
        val allAnswered: Boolean
)