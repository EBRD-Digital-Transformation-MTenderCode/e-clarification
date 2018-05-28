package com.procurement.clarification.model.entity

import java.util.*

data class EnquiryEntity(

        val cpId: String,

        val token: UUID,

        val stage: String,

        val owner: String,

        val jsonData: String,

        val isAnswered: Boolean
)
