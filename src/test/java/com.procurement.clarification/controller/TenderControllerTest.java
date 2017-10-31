package com.procurement.clarification.controller;

import com.procurement.clarification.model.dto.Tender;
import org.junit.jupiter.api.Test;

public class TenderControllerTest extends AbstractDomainObjectTest {

    @Test
    public void tenderRqJsonToObjToJson() {
        compare(
            Tender.class,
            "json/Tender.json");
    }

    @Test
    public void tenderWithoutDefaultsRqJsonToObjToJson(){
        compare(
            Tender.class,
            "json/TenderWithoutDefaults.json");
    }




}
