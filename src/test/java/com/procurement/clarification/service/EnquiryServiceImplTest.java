package com.procurement.clarification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.clarification.exception.ErrorInsertException;
import com.procurement.clarification.model.dto.AuthorDto;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class EnquiryServiceImplTest {
    private final static String ENQUIRY_ID = "44e128a5-ac7a-4c9a-be4c-224b6bf81b20";
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private final static AuthorDto AUTHOR_DTO = new AuthorDto("Author_name", "author_id");
    private final static String TITLE = "title";
    private final static String DESCRIPTION = "description";
    private final static String ANSWER = "anybody answer";
    private final static String RELATED_ID = "related_id";
    private final static String RELATED_LOT = "related_lot";
    private final static String THREAD_ID = "thread_id";
    private static final String OCID = "ocds-213czf-000-00001";

    private final EnquiryPeriodRepository periodRepository = mock(EnquiryPeriodRepository.class);
    private final EnquiryRepository enquiryRepository = mock(EnquiryRepository.class);
    private final ConversionService conversionService = mock(ConversionService.class);

    private final EnquiryServiceImpl enquiryService = new EnquiryServiceImpl(enquiryRepository, periodRepository, new
        JsonUtil
        (new ObjectMapper()), conversionService);

    @Test
    @DisplayName("save Enquiry")
    void saveEnquiryTest() {
        EnquiryDto enquiryDto = new EnquiryDto(ENQUIRY_ID,
                                               LOCAL_DATE_TIME,
                                               AUTHOR_DTO,
                                               TITLE,
                                               DESCRIPTION,
                                               ANSWER,
                                               LOCAL_DATE_TIME,
                                               RELATED_ID,
                                               RELATED_LOT,
                                               THREAD_ID);

        DataDto dataDto = new DataDto(OCID, enquiryDto);

        EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setCpId(OCID);
        enquiryEntity.setEnquiryId(UUID.randomUUID());
        enquiryEntity.setIsAnswered(true);
        enquiryEntity.setJsonData("json");

        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setCpId(OCID);
        enquiryPeriodEntity.setStartDate(LocalDateTime.now()
                                                      .minusDays(2));
        enquiryPeriodEntity.setEndDate(LocalDateTime.now()
                                                    .plusDays(2));

        when(conversionService.convert(dataDto, EnquiryEntity.class)).thenReturn(enquiryEntity);

        EnquiryServiceImpl spy = spy(enquiryService);
        doNothing().when(spy)
                   .checkPeriod(LocalDateTime.now(), OCID);
        when(periodRepository.getByOcId(OCID)).thenReturn(enquiryPeriodEntity);
        enquiryService.saveEnquiry(dataDto);

        verify(enquiryRepository, times(1)).save(enquiryEntity);
    }

    @Test
    @DisplayName("Check Period")
    void checkPeriodTest() {

        when(periodRepository.getByOcId(OCID)).thenThrow(new NullPointerException("nullPointer"));
        assertThrows(NullPointerException.class, () -> enquiryService.checkPeriod(LOCAL_DATE_TIME, OCID));
    }

    @Test
    @DisplayName("Check Period")
    void checkPeriodIncorrectDateTest() {
        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setCpId(OCID);
        enquiryPeriodEntity.setStartDate(LocalDateTime.now()
                                                      .minusDays(3));
        enquiryPeriodEntity.setEndDate(LocalDateTime.now()
                                                    .minusDays(2));

        when(periodRepository.getByOcId(OCID)).thenReturn(enquiryPeriodEntity);
        assertThrows(ErrorInsertException.class, () -> enquiryService.checkPeriod(LOCAL_DATE_TIME, OCID));
    }

}
