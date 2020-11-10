package com.procurement.clarification.infrastructure.api.v2.converter

import com.procurement.clarification.application.model.dto.params.FindEnquiryIdsParams
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.infrastructure.handler.v2.model.request.FindEnquiryIdsRequest

fun FindEnquiryIdsRequest.convert(): Result<FindEnquiryIdsParams, DataErrors> =
    FindEnquiryIdsParams.tryCreate(cpid = this.cpid, ocid = this.ocid, isAnswer = this.isAnswer)
