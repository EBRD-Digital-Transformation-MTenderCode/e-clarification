package com.procurement.clarification.service;

import com.procurement.clarification.exception.ErrorInsertException;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private EnquiryRepository enquiryRepository;
    private EnquiryPeriodRepository periodRepository;
    private JsonUtil jsonUtil;
    private ConversionService conversionService;

    public EnquiryServiceImpl(final EnquiryRepository enquiryRepository,
                              final EnquiryPeriodRepository periodRepository,
                              final JsonUtil jsonUtil,
                              final ConversionService conversionService) {
        this.enquiryRepository = enquiryRepository;
        this.periodRepository = periodRepository;
        this.jsonUtil = jsonUtil;
        this.conversionService = conversionService;
    }

    @Override
    public EnquiryEntity saveEnquiry(final DataDto dataDto) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        checkPeriod(localDateTime, dataDto.getOcid());
        final EnquiryEntity enquiryEntity = conversionService.convert(dataDto, EnquiryEntity.class);
        return enquiryRepository.save(enquiryEntity);
    }

    void checkPeriod(final LocalDateTime localDateTime, final String tenderId) {
        final EnquiryPeriodEntity periodEntity = Optional.ofNullable(periodRepository.getByOcId(tenderId))
            .orElseThrow(() -> new NullPointerException("Period not found"));

            final boolean localDateTimeAfter = localDateTime.isAfter(periodEntity.getStartDate());
            final boolean localDateTimeBefore = localDateTime.isBefore(periodEntity.getEndDate());
            if (!localDateTimeAfter || !localDateTimeBefore) {
                throw new ErrorInsertException("Date not in period.");
            }


    }
}
