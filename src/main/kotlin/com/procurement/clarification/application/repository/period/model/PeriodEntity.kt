package com.procurement.clarification.application.repository.period.model

import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import java.time.LocalDateTime

data class PeriodEntity(
    val cpid: Cpid,
    val ocid: Ocid,
    val owner: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val tenderEndDate: LocalDateTime?
)
