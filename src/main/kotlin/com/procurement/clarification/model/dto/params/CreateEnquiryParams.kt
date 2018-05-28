package com.procurement.clarification.model.dto.params

import com.procurement.clarification.model.dto.CreateEnquiryDto
import java.time.LocalDateTime

data class CreateEnquiryParams(
        val cpId: String,
        val stage: String,
        val date: LocalDateTime,
        val owner: String,
        val dataDto: CreateEnquiryDto
)
