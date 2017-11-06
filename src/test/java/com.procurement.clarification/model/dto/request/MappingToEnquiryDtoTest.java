package com.procurement.clarification.model.dto.request;

import com.procurement.clarification.controller.AbstractDomainObjectTest;
import com.procurement.clarification.model.dto.DataDto;
import org.junit.jupiter.api.Test;

public class MappingToEnquiryDtoTest extends AbstractDomainObjectTest {
    @Test
    public void tenderRqJsonToObjToJson() {
        compare(
            DataDto.class,
            "json/Tender.json");
    }

    @Test
    public void tenderWithoutDefaultsRqJsonToObjToJson(){
        compare(
            DataDto.class,
            "json/TenderWithoutDefaults.json");
    }
}
