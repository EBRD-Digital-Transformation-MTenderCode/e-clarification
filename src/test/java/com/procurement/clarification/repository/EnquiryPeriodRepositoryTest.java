package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.PeriodEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EnquiryPeriodRepositoryTest {
    private static PeriodRepository enquiryPeriodRepository;

    private static PeriodEntity enquiryPeriodEntity;

    @BeforeAll
    static void setUp() {
        enquiryPeriodEntity = new PeriodEntity();
        enquiryPeriodEntity.setOcId("ocds-213czf-000-00001");
        enquiryPeriodEntity.setStartDate(LocalDateTime.now());
        enquiryPeriodEntity.setEndDate(LocalDateTime.now()
                                                    .plusDays(20));
        enquiryPeriodRepository = mock(PeriodRepository.class);
        given(enquiryPeriodRepository.getByOcId(enquiryPeriodEntity.getOcId())).willReturn(enquiryPeriodEntity);
        given(enquiryPeriodRepository.save(enquiryPeriodEntity)).willReturn(enquiryPeriodEntity);
    }

    @Test
    public void save() {
        PeriodEntity result = enquiryPeriodRepository.save(enquiryPeriodEntity);
        assertEquals(result.getOcId(), enquiryPeriodEntity.getOcId());
    }

    @Test
    public void getByOcId() {
        PeriodEntity result = enquiryPeriodRepository.getByOcId(enquiryPeriodEntity.getOcId());
        assertEquals(result.getOcId(), enquiryPeriodEntity.getOcId());
    }
}