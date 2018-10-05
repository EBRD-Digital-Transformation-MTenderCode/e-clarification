package com.procurement.clarification.model.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.ocds.Period
import javax.validation.Valid

data class PeriodRq @JsonCreator constructor(

        @field:Valid
        val enquiryPeriod: Period
)
