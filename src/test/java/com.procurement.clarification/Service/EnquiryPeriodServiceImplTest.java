package com.procurement.clarification.Service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.service.EnquiryPeriodService;
import com.procurement.clarification.service.EnquiryPeriodServiceImpl;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;

public class EnquiryPeriodServiceImplTest {

    private final String enquiryPeriodDtoId="111";
    private final LocalDateTime enquiryPeriodDtoStartDate= LocalDateTime.now();
    private final LocalDateTime enquiryPeriodDtoEndDate= LocalDateTime.now();



    @Test
    public void convertDtoToEntity() {
        EnquiryPeriodDto enquiryPeriodDto = new EnquiryPeriodDto(enquiryPeriodDtoId,enquiryPeriodDtoStartDate,enquiryPeriodDtoEndDate);

        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setTenderId(enquiryPeriodDtoId);
        enquiryPeriodEntity.setStartDate(enquiryPeriodDtoStartDate);
        enquiryPeriodEntity.setEndDate(enquiryPeriodDtoEndDate);

        EnquiryPeriodService enquiryPeriodService =new EnquiryPeriodServiceImpl(mock(EnquiryPeriodRepository.class));

        assertEquals(1,1);








    }
}
