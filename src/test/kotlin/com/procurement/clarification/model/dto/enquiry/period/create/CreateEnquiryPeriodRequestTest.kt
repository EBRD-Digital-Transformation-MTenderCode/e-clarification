package com.procurement.clarification.model.dto.enquiry.period.create

import com.procurement.clarification.infrastructure.handler.enquiry.period.create.CreateEnquiryPeriodRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class CreateEnquiryPeriodRequestTest : AbstractDTOTestBase<CreateEnquiryPeriodRequest>(
    CreateEnquiryPeriodRequest::class.java
) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/get_enquiry_by_ids_request_full.json")
    }
}
