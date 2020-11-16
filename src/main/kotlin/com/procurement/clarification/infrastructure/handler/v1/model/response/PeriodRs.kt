package com.procurement.clarification.infrastructure.handler.v1.model.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.ocds.Tender
import javax.validation.Valid

data class PeriodRs @JsonCreator constructor(

        @field:Valid
        val tender: Tender
)
