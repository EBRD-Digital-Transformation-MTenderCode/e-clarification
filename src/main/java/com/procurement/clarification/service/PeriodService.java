package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.entity.PeriodEntity;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface PeriodService {

    ResponseDto calculateAndSavePeriod(String cpid,
                                       String country,
                                       String pmd,
                                       String stage,
                                       String owner,
                                       LocalDateTime startDate,
                                       LocalDateTime endDate);
    void checkPeriod(String cpid);

    PeriodEntity getPeriod(String cpId);

}
