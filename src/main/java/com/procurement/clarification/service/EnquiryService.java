package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryService {
    //void insertData(DataDto dataDto);
    EnquiryEntity saveEnquiry(DataDto dataDto);
}
