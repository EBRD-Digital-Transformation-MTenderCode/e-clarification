package com.procurement.clarification.infrastructure.model.dto.period.create.request

import com.procurement.clarification.application.model.dto.period.create.CreatePeriodData

fun CreatePeriodRequest.convert() = CreatePeriodData(
    period = this.period.let { period ->
        CreatePeriodData.Period(
            startDate = period.startDate,
            endDate = period.endDate
        )
    }
)
