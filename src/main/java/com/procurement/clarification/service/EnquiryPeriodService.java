package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.PeriodDataDto;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryPeriodService {

    void saveEnquiryPeriod(EnquiryPeriodDto data);

    void calculateAndSaveEnquiryPeriod(PeriodDataDto data);
}
