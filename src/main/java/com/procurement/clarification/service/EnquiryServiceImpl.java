package com.procurement.clarification.service;

import com.procurement.clarification.exception.ErrorInsertException;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private EnquiryRepository enquiryRepository;
    private EnquiryPeriodRepository periodRepository;
    private JsonUtil jsonUtil;
    private ConversionService conversionService;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository,
                              EnquiryPeriodRepository periodRepository,
                              JsonUtil jsonUtil,
                              ConversionService conversionService) {
        this.enquiryRepository = enquiryRepository;
        this.periodRepository = periodRepository;
        this.jsonUtil = jsonUtil;
        this.conversionService = conversionService;
    }

    @Override
    public EnquiryEntity saveEnquiry(DataDto dataDto) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        checkPeriod(localDateTime, dataDto.getOcid());
        EnquiryEntity enquiryEntity = conversionService.convert(dataDto, EnquiryEntity.class);
        return enquiryRepository.save(enquiryEntity);//todo в тестах замокать репозиторий и проверить что сейв
        // вызывается с моими параметрами только один раз
    }

    private void checkPeriod(final LocalDateTime localDateTime, final String tenderId) {
        final EnquiryPeriodEntity periodEntity = periodRepository.getByOcId(tenderId);
        boolean localDateTimeAfter = localDateTime.isAfter(periodEntity.getStartDate());
        boolean localDateTimeBefore = localDateTime.isBefore(periodEntity.getEndDate());
        if (!localDateTimeAfter && !localDateTimeBefore) {
            throw new ErrorInsertException("Date not in period.");
        }
    }
}
