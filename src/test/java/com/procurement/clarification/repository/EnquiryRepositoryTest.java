package com.procurement.clarification.repository;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.clarification.JsonUtil;
import com.procurement.clarification.model.entity.EnquiryEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EnquiryRepositoryTest {
    private static EnquiryRepository enquiryRepository;

    private static EnquiryEntity enquiryEntity;

    @BeforeAll
    static void setUp() {
        enquiryEntity = new EnquiryEntity();
        enquiryEntity.setCpId("ocds-213czf-000-00001");
        enquiryEntity.setEnquiryId(UUIDs.timeBased());
        enquiryEntity.setIsAnswered(true);
        enquiryEntity.setJsonData(new JsonUtil().getResource("json/data.json"));
        enquiryRepository = mock(EnquiryRepository.class);
        given(enquiryRepository.save(enquiryEntity)).willReturn(enquiryEntity);
    }

    @Test
    public void save() {
        EnquiryEntity result = enquiryRepository.save(enquiryEntity);
        assertEquals(result.getCpId(), enquiryEntity.getCpId());
    }
}