package com.procurement.clarification.infrastructure.handler.enquiry.period.create

import com.procurement.clarification.application.model.dto.params.CreateEnquiryPeriodParams
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asSuccess

fun CreateEnquiryPeriodRequest.convert(): Result<CreateEnquiryPeriodParams, DataErrors> =
    CreateEnquiryPeriodParams.tryCreate(
        cpid = cpid,
        ocid = ocid,
        tender = tender.convert().orForwardFail { fail -> return fail },
        country = country,
        operationType = operationType,
        pmd = pmd,
        owner = owner
    )

private fun CreateEnquiryPeriodRequest.Tender.convert(): Result<CreateEnquiryPeriodParams.Tender, DataErrors> {
    val tenderPeriod = CreateEnquiryPeriodParams.Tender.TenderPeriod.tryCreate(
        startDate = tenderPeriod.startDate,
        endDate = tenderPeriod.endDate
    ).orForwardFail { fail -> return fail }

    return CreateEnquiryPeriodParams.Tender(tenderPeriod).asSuccess()
}


