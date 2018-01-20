package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.CreateEnquiryParams;
import com.procurement.clarification.model.dto.UpdateEnquiryParams;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryService {

    ResponseDto saveEnquiry(CreateEnquiryParams params);

    ResponseDto updateEnquiry(UpdateEnquiryParams params);

    ResponseDto checkEnquiries(String cpId, String stage);
}
