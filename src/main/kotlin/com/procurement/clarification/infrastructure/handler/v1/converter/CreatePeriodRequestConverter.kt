package com.procurement.clarification.infrastructure.handler.v1.converter

import com.procurement.clarification.application.model.dto.period.create.CreatePeriodData
import com.procurement.clarification.infrastructure.handler.v1.model.request.CreatePeriodRequest

fun CreatePeriodRequest.convert() = CreatePeriodData(
    period = this.period.let { period ->
        CreatePeriodData.Period(
            startDate = period.startDate,
            endDate = period.endDate
        )
    }
)
