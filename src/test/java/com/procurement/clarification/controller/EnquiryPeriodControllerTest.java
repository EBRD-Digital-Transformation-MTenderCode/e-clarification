package com.procurement.clarification.controller;

import com.procurement.clarification.JsonUtil;
import com.procurement.clarification.service.EnquiryPeriodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnquiryPeriodControllerTest {

    @Test
    @DisplayName("Test /enquiryPeriod/save status: 201 - Created")
    void saveEnquiryPeriodStatusCreated() throws Exception {
        final EnquiryPeriodService enquiryPeriodService = mock(EnquiryPeriodService.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EnquiryPeriodController(enquiryPeriodService))
                                         .build();
        mockMvc.perform(post("/enquiryPeriod/save")
                            .content(new JsonUtil().getResource("json/enquiry-period.json"))
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test /enquiryPeriod/save status: 400 - Bad Request")
    void saveEnquiryPeriodStatusBadRequest() throws Exception {
        final EnquiryPeriodService enquiryPeriodService = mock(EnquiryPeriodService.class);
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EnquiryPeriodController(enquiryPeriodService))
                                         .setControllerAdvice(handler)
                                         .build();
        mockMvc.perform(post("/enquiryPeriod/save")
                            .content("{ }")
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Houston we have a problem"));
    }

    @Test
    @DisplayName("Test /enquiryPeriod/calculateAndSave status: 201 - Created")
    void calculateAndSaveEnquiryPeriodStatusCreated() throws Exception {
        final EnquiryPeriodService enquiryPeriodService = mock(EnquiryPeriodService.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EnquiryPeriodController(enquiryPeriodService))
                                         .build();
        mockMvc.perform(post("/enquiryPeriod/calculateAndSave")
                            .content(new JsonUtil().getResource("json/enquiry-period.json"))
                            .param("iso", "UA")
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test /enquiryPeriod/calculateAndSave status: 400 - Bad Request")
    void calculateAndSaveEnquiryPeriodBadRequest() throws Exception {
        final EnquiryPeriodService enquiryPeriodService = mock(EnquiryPeriodService.class);
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EnquiryPeriodController(enquiryPeriodService))
                                         .setControllerAdvice(handler)
                                         .build();
        mockMvc.perform(post("/enquiryPeriod/calculateAndSave")
                            .content("{ }")
                            .param("iso", "UA")
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Houston we have a problem"));
    }
}