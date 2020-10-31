package com.procurement.clarification.model.dto.enquiry.id.find

import com.procurement.clarification.infrastructure.handler.enquiry.id.find.FindEnquiryIdsRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class FindEnquiryIdsTest : AbstractDTOTestBase<FindEnquiryIdsRequest>(
    FindEnquiryIdsRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/id/find_enquiry_ids_request_full.json")
    }
    @Test
    fun fully1() {
        testBindingAndMapping("json/dto/enquiry/id/find_enquiry_ids_request_1.json")
    }

}
