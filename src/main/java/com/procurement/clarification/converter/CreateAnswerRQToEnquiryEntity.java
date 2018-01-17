package com.procurement.clarification.converter;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.clarification.model.dto.CreateAnswerRQ;
import com.procurement.clarification.model.dto.CreateAnswerRQDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.utils.JsonUtil;
import org.springframework.core.convert.converter.Converter;

public class CreateAnswerRQToEnquiryEntity implements Converter<CreateAnswerRQ, EnquiryEntity> {

    @Override
    public EnquiryEntity convert(final CreateAnswerRQ dataDto) {
        final CreateAnswerRQDto enquiryDto = dataDto.getDataDto();

        final EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setCpId(dataDto.getCpid());
        enquiryEntity.setEnquiryId(UUIDs.timeBased());

        enquiryEntity.setIsAnswered(false);

        enquiryEntity.setJsonData(new JsonUtil(new ObjectMapper()).toJson(enquiryDto));
        return enquiryEntity;
    }
}
