package com.procurement.clarification.model.dto.enquiry.period.create

import com.procurement.clarification.infrastructure.handler.enquiry.period.create.CreateEnquiryPeriodResult
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class CreateEnquiryPeriodResultTest : AbstractDTOTestBase<CreateEnquiryPeriodResult>(
    CreateEnquiryPeriodResult::class.java
) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/get_enquiry_by_ids_request_full.json")
    }
}
