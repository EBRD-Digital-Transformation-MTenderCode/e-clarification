package com.procurement.clarification.converter;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import java.util.Objects;
import org.springframework.core.convert.converter.Converter;

public class EnquiryPeriodDtoToEnquiryPeriodEntity implements Converter<EnquiryPeriodDto, EnquiryPeriodEntity> {

    @Override
    public EnquiryPeriodEntity convert(final EnquiryPeriodDto enquiryPeriodDto) {
        Objects.requireNonNull(enquiryPeriodDto);
        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setOcId(enquiryPeriodDto.getOcId());
        enquiryPeriodEntity.setStartDate(enquiryPeriodDto.getStartDate());
        enquiryPeriodEntity.setEndDate(enquiryPeriodDto.getEndDate());
        return enquiryPeriodEntity;
    }
}
