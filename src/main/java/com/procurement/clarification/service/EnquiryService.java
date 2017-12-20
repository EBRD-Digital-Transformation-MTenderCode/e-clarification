package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.DataDto;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryService {
    void insertData(DataDto dataDto);

    Boolean checkEnquiries(String ocid);
}
