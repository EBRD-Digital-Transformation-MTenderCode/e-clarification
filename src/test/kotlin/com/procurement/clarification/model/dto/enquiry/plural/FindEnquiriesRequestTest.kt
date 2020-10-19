package com.procurement.clarification.model.dto.enquiry.plural

import com.procurement.clarification.infrastructure.handler.enquiry.find.plural.FindEnquiriesRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class FindEnquiriesRequestTest : AbstractDTOTestBase<FindEnquiriesRequest>(
    FindEnquiriesRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/plural/find_enquiries_request_full.json")
    }
    @Test
    fun fully1() {
        testBindingAndMapping("json/dto/enquiry/plural/find_enquiries_request_1.json")
    }

}
