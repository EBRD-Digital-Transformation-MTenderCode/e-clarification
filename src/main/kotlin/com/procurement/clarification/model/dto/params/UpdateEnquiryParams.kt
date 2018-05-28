package com.procurement.clarification.model.dto.params

import com.procurement.clarification.model.dto.UpdateEnquiryDto
import java.time.LocalDateTime


data class UpdateEnquiryParams(
        val cpId: String,
        val stage: String,
        val token: String,
        val dateTime: LocalDateTime,
        val owner: String,
        val data: UpdateEnquiryDto
)
