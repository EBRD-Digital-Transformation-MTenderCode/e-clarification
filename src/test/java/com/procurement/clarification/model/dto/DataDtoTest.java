package com.procurement.clarification.model.dto;

import com.procurement.clarification.AbstractDomainObjectTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DataDtoTest extends AbstractDomainObjectTest {

    @Test
    @DisplayName("Testing mapping json to DataDto and to json")
    public void testJsonToDataDtoToJson() {
        compare(DataDto.class, "json/data.json");
    }

}