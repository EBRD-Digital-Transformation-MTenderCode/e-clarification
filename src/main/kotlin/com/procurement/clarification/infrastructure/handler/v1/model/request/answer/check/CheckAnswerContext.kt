package com.procurement.clarification.infrastructure.handler.v1.model.request.answer.check

import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.token.Token
import java.time.LocalDateTime

class CheckAnswerContext(
    val cpid: Cpid,
    val ocid: Ocid,
    val token: Token,
    val owner: String,
    val startDate: LocalDateTime,
    val enquiryId: String
)