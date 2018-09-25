package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.ocds.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class PeriodRs @JsonCreator constructor(

        @field:Valid
        val tender: Tender
)
