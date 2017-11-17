package com.procurement.clarification.converter;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnquiryPeriodDtoToEnquiryPeriodEntityTest {



    @Test
    @DisplayName("Converter")
    void convertTest() {

         final String ocId = "ocds-213czf-000-00001";
         final LocalDateTime start = LocalDateTime.now();
         final LocalDateTime end = LocalDateTime.now();
        EnquiryPeriodDto enquiryPeriodDto = new EnquiryPeriodDto(ocId,
                                                                 start,
                                                                 end);
        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setOcId(ocId);
        enquiryPeriodEntity.setStartDate(start);
        enquiryPeriodEntity.setEndDate(end);

        EnquiryPeriodEntity enquiryPeriodEntityConvert = new EnquiryPeriodDtoToEnquiryPeriodEntity().convert(enquiryPeriodDto);

        assertEquals(enquiryPeriodEntity,enquiryPeriodEntityConvert);


    }
}
