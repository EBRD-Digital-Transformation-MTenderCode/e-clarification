package com.procurement.clarification.model.entity

import java.util.*

data class EnquiryEntity(

        val cpId: String,

        val stage: String,

        val token: UUID,

        val owner: String,

        val jsonData: String,

        val isAnswered: Boolean
)
