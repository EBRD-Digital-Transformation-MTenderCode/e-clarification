package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.CreateAnswerRQ;
import com.procurement.clarification.model.dto.CreateAnswerRSDto;
import com.procurement.clarification.model.dto.UpdateAnswerRQ;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryService {
    ResponseDto saveEnquiry(CreateAnswerRQ createAnswerRQ);
    ResponseDto updateEnquiry(UpdateAnswerRQ updateAnswerRQ);
}
