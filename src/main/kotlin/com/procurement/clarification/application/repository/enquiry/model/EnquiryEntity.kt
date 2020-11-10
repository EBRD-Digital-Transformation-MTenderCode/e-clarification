package com.procurement.clarification.application.repository.enquiry.model

import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.token.Token

data class EnquiryEntity(
    val cpid: Cpid,
    val ocid: Ocid,
    val token: Token,
    val isAnswered: Boolean,
    val jsonData: String
)
