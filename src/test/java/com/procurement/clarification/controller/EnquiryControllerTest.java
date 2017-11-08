package com.procurement.clarification.controller;

import com.procurement.clarification.JsonUtil;
import com.procurement.clarification.service.EnquiryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnquiryControllerTest {

    @Test
    @DisplayName("Test /enquiry/save status: 201 - Created")
    void saveEnquiryStatusCreated() throws Exception {
        final EnquiryService enquiryService = mock(EnquiryService.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EnquiryController(enquiryService))
                                         .build();
        mockMvc.perform(post("/enquiry/save")
                            .content(new JsonUtil().getResource("json/data.json"))
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test /enquiry/save status: 400 - Bad Request")
    void saveEnquiryStatusBadRequest() throws Exception {
        final EnquiryService enquiryService = mock(EnquiryService.class);
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EnquiryController(enquiryService))
                                         .setControllerAdvice(handler)
                                         .build();
        mockMvc.perform(post("/enquiry/save")
                            .content("{ }")
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Houston we have a problem"));
    }
}