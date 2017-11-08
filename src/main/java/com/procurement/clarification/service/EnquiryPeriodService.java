package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryPeriodService {

    void saveEnquiryPeriod(EnquiryPeriodDto data);

    void calculateAndSaveEnquiryPeriod(EnquiryPeriodDto data, String iso);
}
