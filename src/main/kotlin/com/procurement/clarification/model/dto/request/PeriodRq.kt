package com.procurement.clarification.model.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.ocds.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class PeriodRq @JsonCreator constructor(

        @field:Valid
        val enquiryPeriod: Period
)
