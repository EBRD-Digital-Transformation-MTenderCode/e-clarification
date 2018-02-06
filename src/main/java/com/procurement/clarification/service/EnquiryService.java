package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.params.CreateEnquiryParams;
import com.procurement.clarification.model.dto.params.UpdateEnquiryParams;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryService {

    ResponseDto saveEnquiry(CreateEnquiryParams params);

    ResponseDto updateEnquiry(UpdateEnquiryParams params);

    ResponseDto checkEnquiries(String cpId, String stage);
}
