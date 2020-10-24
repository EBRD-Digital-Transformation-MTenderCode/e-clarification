package com.procurement.clarification.model.dto.enquiry.period.create

import com.procurement.clarification.infrastructure.handler.enquiry.period.create.CreateEnquiryPeriodResult
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class CreateEnquiryPeriodResultTest : AbstractDTOTestBase<CreateEnquiryPeriodResult>(
    CreateEnquiryPeriodResult::class.java
) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/period/create/result_create_enquiry_period_full.json")
    }
}
