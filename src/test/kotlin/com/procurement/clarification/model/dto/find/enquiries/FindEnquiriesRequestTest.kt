package com.procurement.clarification.model.dto.find.enquiries

import com.procurement.clarification.infrastructure.handler.enquiry.find.plural.FindEnquiriesRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class FindEnquiriesRequestTest : AbstractDTOTestBase<FindEnquiriesRequest>(
    FindEnquiriesRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/find/enquirires/find_enquiries_request_full.json")
    }
    @Test
    fun fully1() {
        testBindingAndMapping("json/dto/find/enquirires/find_enquiries_request_1.json")
    }

}
