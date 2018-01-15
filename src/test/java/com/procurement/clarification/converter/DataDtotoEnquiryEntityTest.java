package com.procurement.clarification.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.clarification.model.dto.AuthorDto;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataDtotoEnquiryEntityTest {
    private final static String ENQUIRY_ID = "44e128a5-ac7a-4c9a-be4c-224b6bf81b20";
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private final static AuthorDto AUTHOR_DTO = new AuthorDto("Author_name", "author_id");
    private final static String TITLE = "title";
    private final static String DESCRIPTION = "description";
    private final static String ANSWER = "anybody answer";
    private final static String RELATED_ID = "related_id";
    private final static String RELATED_LOT = "related_lot";
    private final static String THREAD_ID = "thread_id";
    public static final String OCID = "ocds-213czf-000-00001";

    private static EnquiryDto enquiryDto;

    private final DataDtotoEnquiryEntity dataDtotoEnquiryEntity = new DataDtotoEnquiryEntity();



    @BeforeAll
    static void init() {
        enquiryDto = new EnquiryDto(ENQUIRY_ID,
                                    LOCAL_DATE_TIME,
                                    AUTHOR_DTO,
                                    TITLE,
                                    DESCRIPTION,
                                    ANSWER,
                                    LOCAL_DATE_TIME,
                                    RELATED_ID,
                                    RELATED_LOT,
                                    THREAD_ID);
    }

    @Test
    @DisplayName("Converter")
    void convertTest(){
        DataDto dataDto  = new DataDto(OCID, enquiryDto);
        EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setCpId(OCID);
        enquiryEntity.setEnquiryId(dataDtotoEnquiryEntity.getUUID(enquiryDto));
        enquiryEntity.setJsonData(new JsonUtil(new ObjectMapper()).toJson(enquiryDto));
        enquiryEntity.setIsAnswered(true);

        EnquiryEntity enquiryEntityConverted = dataDtotoEnquiryEntity.convert(dataDto);

        assertEquals(enquiryEntity,enquiryEntityConverted);


    }

    @Test
    @DisplayName("enquiry_id is present")
    void getUUIDTrueTest() {

        final UUID uuid = dataDtotoEnquiryEntity.getUUID(enquiryDto);
        assertEquals(uuid, UUID.fromString(enquiryDto.getId()));
    }

    @Test
    @DisplayName("enquiry_id is not present")
    void getUUIDFalseTest() {
        final EnquiryDto enquiryDto = new EnquiryDto(null,
                                                     LOCAL_DATE_TIME,
                                                     AUTHOR_DTO,
                                                     TITLE,
                                                     DESCRIPTION,
                                                     ANSWER,
                                                     LOCAL_DATE_TIME,
                                                     RELATED_ID,
                                                     RELATED_LOT,
                                                     THREAD_ID);

        UUID uuid = dataDtotoEnquiryEntity.getUUID(enquiryDto);
        assertNotNull(uuid);
    }

    @Test
    @DisplayName("date is not present")
    void setDateTest() {
        final EnquiryDto enquiryDto = new EnquiryDto(ENQUIRY_ID,
                                                     null,
                                                     AUTHOR_DTO,
                                                     TITLE,
                                                     DESCRIPTION,
                                                     ANSWER,
                                                     LOCAL_DATE_TIME,
                                                     RELATED_ID,
                                                     RELATED_LOT,
                                                     THREAD_ID);

        dataDtotoEnquiryEntity.setDate(enquiryDto, LocalDateTime.now());
        assertNotNull(enquiryDto.getDate());
    }

    @Test
    @DisplayName("its answered")
    void setAnsweredTrueTest() {

        assertTrue(dataDtotoEnquiryEntity.setAnswered(enquiryDto));
    }

    @Test
    @DisplayName("its not answered")
    void setAnsweredFalseTest() {
        final EnquiryDto enquiryDto = new EnquiryDto(ENQUIRY_ID,
                                                     LOCAL_DATE_TIME,
                                                     AUTHOR_DTO,
                                                     TITLE,
                                                     DESCRIPTION,
                                                     null,
                                                     LOCAL_DATE_TIME,
                                                     RELATED_ID,
                                                     RELATED_LOT,
                                                     THREAD_ID);

        assertFalse(dataDtotoEnquiryEntity.setAnswered(enquiryDto));
    }
}
