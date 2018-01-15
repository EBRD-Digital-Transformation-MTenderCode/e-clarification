package com.procurement.clarification.converter;

import com.procurement.clarification.model.dto.PeriodDataDto;
import com.procurement.clarification.model.dto.TenderPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeriodDataDtoToEnquiryPeriodEntityTest {




    @Test
    @DisplayName("converter")
    void convert(){
        final String ocId = "ocds-213czf-000-00001";
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = LocalDateTime.now();
        final TenderPeriodDto tenderPeriodDto = new TenderPeriodDto(start,end);

        PeriodDataDto periodDataDto = new PeriodDataDto(ocId,null,null,tenderPeriodDto);

        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();

        enquiryPeriodEntity.setCpId(ocId);
        enquiryPeriodEntity.setStartDate(start);
        enquiryPeriodEntity.setEndDate(end);

        EnquiryPeriodEntity enquiryPeriodEntityConvert = new PeriodDataDtoToEnquiryPeriodEntity().convert(periodDataDto);

        assertEquals(enquiryPeriodEntity,enquiryPeriodEntityConvert);

    }
}
