package com.procurement.clarification.model.dto.request;

import com.procurement.clarification.controller.AbstractDomainObjectTest;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import org.junit.jupiter.api.Test;

public class MappingToEnquiryPeriodDtoTest extends AbstractDomainObjectTest {
    @Test
    public void enquiryPeriodRqJsonToObjToJson(){
        compare(EnquiryPeriodDto.class,
                "json/EnquiryPeriod.json");
    }
}
