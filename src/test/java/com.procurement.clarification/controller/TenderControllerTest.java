package com.procurement.clarification.controller;

import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.Tender;
import java.util.Date;
import javax.xml.crypto.Data;
import org.junit.jupiter.api.Test;

public class TenderControllerTest extends AbstractDomainObjectTest {

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

    @Test
    public void enquiryPeriodRqJsonToObjToJson(){
        compare(EnquiryPeriodDto.class,
                "json/EnquiryPeriod.json");
    }




}
