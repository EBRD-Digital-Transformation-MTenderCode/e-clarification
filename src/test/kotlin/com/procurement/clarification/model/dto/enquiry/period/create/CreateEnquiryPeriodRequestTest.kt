package com.procurement.clarification.model.dto.enquiry.period.create

import com.procurement.clarification.infrastructure.handler.v2.model.request.CreateEnquiryPeriodRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class CreateEnquiryPeriodRequestTest : AbstractDTOTestBase<CreateEnquiryPeriodRequest>(
    CreateEnquiryPeriodRequest::class.java
) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/period/create/request_create_enquiry_period_full.json")
    }
}
