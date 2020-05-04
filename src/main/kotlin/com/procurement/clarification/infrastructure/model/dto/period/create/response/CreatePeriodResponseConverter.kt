package com.procurement.clarification.infrastructure.model.dto.period.create.response

import com.procurement.clarification.application.model.dto.period.create.CreatePeriodResult

fun CreatePeriodResult.convert() = CreatePeriodResponse(
    enquiryPeriod = this.enquiryPeriod.let { period ->
        CreatePeriodResponse.Period(
            startDate = period.startDate,
            endDate = period.endDate
        )
    }
)
