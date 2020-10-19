package com.procurement.clarification.infrastructure.handler.enquiry.find

import com.procurement.clarification.application.model.dto.params.FindEnquiriesParams
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.util.Result

fun FindEnquiriesRequest.convert(): Result<FindEnquiriesParams, DataErrors> =
    FindEnquiriesParams.tryCreate(cpid = this.cpid, ocid = this.ocid, isAnswer = this.isAnswer)

