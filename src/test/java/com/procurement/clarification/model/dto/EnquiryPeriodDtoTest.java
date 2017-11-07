package com.procurement.clarification.model.dto;

import com.procurement.clarification.AbstractDomainObjectTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EnquiryPeriodDtoTest extends AbstractDomainObjectTest {

    @Test
    @DisplayName("Testing mapping json to ContractProcessPeriodDto and to json")
    public void testJsonToContractProcessPeriodDtoToJson() {
        compare(EnquiryPeriodDto.class, "json/enquiry-period.json");
    }
}