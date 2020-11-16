package com.procurement.clarification.model.dto.enquiry.find

import com.procurement.clarification.infrastructure.handler.v2.model.request.FindEnquiriesRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class FindEnquiriesRequestTest : AbstractDTOTestBase<FindEnquiriesRequest>(
    FindEnquiriesRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/find/find_enquiries_request_full.json")
    }
    @Test
    fun fully1() {
        testBindingAndMapping("json/dto/enquiry/find/find_enquiries_request_1.json")
    }

}
