package com.procurement.clarification.model.entity

import java.util.*

data class EnquiryEntity(

        val cpId: String,

        val token_entity: UUID,

        val stage: String,

        var jsonData: String,

        var isAnswered: Boolean
)
