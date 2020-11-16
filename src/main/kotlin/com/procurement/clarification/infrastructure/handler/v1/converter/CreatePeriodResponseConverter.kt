package com.procurement.clarification.infrastructure.api.v1.converter

import com.procurement.clarification.application.model.dto.period.create.CreatePeriodResult
import com.procurement.clarification.infrastructure.handler.v1.model.response.CreatePeriodResponse

fun CreatePeriodResult.convert() = CreatePeriodResponse(
    enquiryPeriod = this.enquiryPeriod.let { period ->
        CreatePeriodResponse.Period(
            startDate = period.startDate,
            endDate = period.endDate
        )
    }
)
