package com.procurement.clarification.converter;

import com.procurement.clarification.model.dto.PeriodDataDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import org.springframework.core.convert.converter.Converter;

public class PeriodDataDtoToEnquiryPeriodEntity implements Converter<PeriodDataDto, EnquiryPeriodEntity> {

    @Override
    public EnquiryPeriodEntity convert(final PeriodDataDto periodDataDto) {

        final EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setCpId(periodDataDto.getOcId());
        enquiryPeriodEntity.setStartDate(periodDataDto.getTenderPeriod()
                                                      .getStartDate());
        enquiryPeriodEntity.setEndDate(periodDataDto.getTenderPeriod()
                                                    .getEndDate());
        return enquiryPeriodEntity;
    }
}
