package com.procurement.clarification.model.dto.enquiry.get

import com.procurement.clarification.infrastructure.handler.enquiry.get.GetEnquiryByIdsResult
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class GetEnquiryByIdsResultTest : AbstractDTOTestBase<GetEnquiryByIdsResult>(
    GetEnquiryByIdsResult::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/enquiry/get/get_enquiry_by_ids_response_full.json")
    }

    @Test
    fun fully1() {
        testBindingAndMapping("json/dto/enquiry/get/get_enquiry_by_ids_response_1.json")
    }

    @Test
    fun fully2() {
        testBindingAndMapping("json/dto/enquiry/get/get_enquiry_by_ids_response_2.json")
    }

}
