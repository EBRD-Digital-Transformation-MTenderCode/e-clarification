package com.procurement.clarification.model.dto.enquiry.id.get

import com.procurement.clarification.infrastructure.handler.enquiry.id.get.GetEnquiryByIdsRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class GetEnquiryByIdsRequestTest : AbstractDTOTestBase<GetEnquiryByIdsRequest>(
    GetEnquiryByIdsRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/get.enquirybyids/get_enquiry_by_ids_request_full.json")
    }

}
