package com.procurement.clarification.model.dto.enquiry.plural

import com.procurement.clarification.infrastructure.handler.enquiry.find.plural.FindEnquiriesResult
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class FindEnquiriesResultTest : AbstractDTOTestBase<FindEnquiriesResult>(
    FindEnquiriesResult::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/find/enquirires/find_enquiries_result_full.json")
    }

    @Test
    fun fully1() {
        testBindingAndMapping("json/dto/find/enquirires/find_enquiries_result_1.json")
    }

    @Test
    fun fully2() {
        testBindingAndMapping("json/dto/find/enquirires/find_enquiries_result_2.json")
    }
}
