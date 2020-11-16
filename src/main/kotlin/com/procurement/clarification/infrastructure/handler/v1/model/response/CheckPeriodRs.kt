package com.procurement.clarification.infrastructure.handler.v1.model.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CheckPeriodRs @JsonCreator constructor(

        @get:JsonProperty("setExtendedPeriod")
        val setExtendedPeriod: Boolean,

        @get:JsonProperty("isEnquiryPeriodChanged")
        val isEnquiryPeriodChanged: Boolean,

        val startDate: LocalDateTime,

        val endDate: LocalDateTime
)
