package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.model.dto.ocds.Period

data class CheckPeriodRs @JsonCreator constructor(

        @get:JsonProperty("setExtendedPeriod")
        val setExtendedPeriod: Boolean?,

        @get:JsonProperty("isEnquiryPeriodChange")
        val isEnquiryPeriodChange: Boolean?,

        val enquiryPeriodEndDate: Period
)
