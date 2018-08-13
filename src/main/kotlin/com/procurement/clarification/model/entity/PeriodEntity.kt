package com.procurement.clarification.model.entity

import java.util.*

data class PeriodEntity(

        val cpId: String,

        val stage: String,

        val owner:String,

        val startDate: Date,

        val endDate: Date,

        val tenderEndDate: Date
)
