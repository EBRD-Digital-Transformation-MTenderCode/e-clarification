package com.procurement.clarification.converter;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;

public class DataDtotoEnquiryEntity implements Converter<DataDto, EnquiryEntity> {

    @Override
    public EnquiryEntity convert(final DataDto dataDto) {
        EnquiryDto enquiryDto = dataDto.getEnquiry();

        EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setOcId(dataDto.getOcid());
        enquiryEntity.setEnquiryId(getUUID(enquiryDto));

        setDate(enquiryDto, LocalDateTime.now());

        enquiryEntity.setIsAnswered(setAnswered(enquiryDto));

        enquiryEntity.setJsonData(new JsonUtil(new ObjectMapper()).toJson(enquiryDto));
        return enquiryEntity;
    }

    UUID getUUID(EnquiryDto enquiryDto) {
        return Objects.isNull(enquiryDto.getId())
               ? UUIDs.timeBased()
               : UUID.fromString(enquiryDto.getId());
    }

    void setDate(EnquiryDto enquiryDto, final LocalDateTime localDateTime) {
        if (Objects.isNull(enquiryDto.getDate())) {
            enquiryDto.setDate(localDateTime);
        }
    }

    boolean setAnswered(EnquiryDto enquiryDto) {
        if (Objects.nonNull(enquiryDto.getAnswer()) && Objects.nonNull(enquiryDto.getDateAnswered())) {
            return true;
        }
        return false;
    }
}
