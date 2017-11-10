package com.procurement.clarification.model.dto;

import com.procurement.clarification.AbstractDomainObjectTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EnquiryPeriodDtoTest extends AbstractDomainObjectTest {

    @Test
    @DisplayName("Testing mapping json to EnquiryPeriodDtoTest and to json")
    public void testJsonToEnquiryPeriodDtoToJson() {
        compare(EnquiryPeriodDto.class, "json/enquiry-period.json");
    }
}