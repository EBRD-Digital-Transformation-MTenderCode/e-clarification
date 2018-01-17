package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.bpe.ResponseDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryPeriodService {

    ResponseDto calculateAndSaveEnquiryPeriod(String cpId,
                                              String country,
                                              String stage,
                                              String pmd,
                                              LocalDateTime startDate,
                                              LocalDateTime endDate,
                                              String owner);
}
