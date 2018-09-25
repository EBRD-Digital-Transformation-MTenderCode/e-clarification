package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.model.dto.ocds.Tender

data class CheckPeriodRs @JsonCreator constructor(

        @get:JsonProperty("setExtendedPeriod")
        val setExtendedPeriod: Boolean,

        @get:JsonProperty("isEnquiryPeriodChanged")
        val isEnquiryPeriodChanged: Boolean,

        val tender: Tender?
)
