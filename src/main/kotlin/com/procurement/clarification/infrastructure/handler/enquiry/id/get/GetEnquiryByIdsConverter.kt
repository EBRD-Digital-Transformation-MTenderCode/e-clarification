package com.procurement.clarification.infrastructure.handler.enquiry.id.get

import com.procurement.clarification.application.model.dto.params.GetEnquiryByIdsParams
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.util.Result

fun GetEnquiryByIdsRequest.convert(): Result<GetEnquiryByIdsParams, DataErrors> =
    GetEnquiryByIdsParams.tryCreate(cpid = this.cpid, ocid = this.ocid, enquiryIds = this.enquiryIds)
