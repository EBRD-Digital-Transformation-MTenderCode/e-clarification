package com.procurement.clarification.controller;

import com.procurement.clarification.service.EnquiryPeriodService;
import com.procurement.clarification.service.EnquiryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TenderControllerTest {

    @Test
    @DisplayName("Test TenderController status: 201 - Created")
    void testEnquiryCreated() throws Exception {
        final EnquiryService enquiryService = mock(EnquiryService.class);
        final EnquiryPeriodService enquiryPeriodService = mock(EnquiryPeriodService.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TenderController(enquiryPeriodService, enquiryService))
                                         .build();
        mockMvc.perform(post("/tenders")
                            .content(new JsonUtil().getResource("json/Tender.json"))
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
    }

}
