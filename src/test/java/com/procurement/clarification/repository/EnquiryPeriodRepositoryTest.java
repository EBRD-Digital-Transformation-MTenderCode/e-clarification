package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EnquiryPeriodRepositoryTest {
    private static EnquiryPeriodRepository enquiryPeriodRepository;

    private static EnquiryPeriodEntity enquiryPeriodEntity;

    @BeforeAll
    static void setUp() {
        enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setCpId("ocds-213czf-000-00001");
        enquiryPeriodEntity.setStartDate(LocalDateTime.now());
        enquiryPeriodEntity.setEndDate(LocalDateTime.now()
                                                    .plusDays(20));
        enquiryPeriodRepository = mock(EnquiryPeriodRepository.class);
        given(enquiryPeriodRepository.getByOcId(enquiryPeriodEntity.getCpId())).willReturn(enquiryPeriodEntity);
        given(enquiryPeriodRepository.save(enquiryPeriodEntity)).willReturn(enquiryPeriodEntity);
    }

    @Test
    public void save() {
        EnquiryPeriodEntity result = enquiryPeriodRepository.save(enquiryPeriodEntity);
        assertEquals(result.getCpId(), enquiryPeriodEntity.getCpId());
    }

    @Test
    public void getByOcId() {
        EnquiryPeriodEntity result = enquiryPeriodRepository.getByOcId(enquiryPeriodEntity.getCpId());
        assertEquals(result.getCpId(), enquiryPeriodEntity.getCpId());
    }
}