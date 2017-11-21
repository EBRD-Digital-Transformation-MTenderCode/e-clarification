package com.procurement.clarification.service;

import com.procurement.clarification.exception.PeriodException;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.PeriodDataDto;
import com.procurement.clarification.model.dto.TenderPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.RulesRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class EnquiryPeriodServiceImpl implements EnquiryPeriodService {

    private EnquiryPeriodRepository enquiryPeriodRepository;
    private RulesRepository rulesRepository;
    private ConversionService conversionService;

    public EnquiryPeriodServiceImpl(EnquiryPeriodRepository enquiryPeriodRepository,
                                    RulesRepository rulesRepository,
                                    ConversionService conversionService) {
        this.enquiryPeriodRepository = enquiryPeriodRepository;
        this.rulesRepository = rulesRepository;
        this.conversionService = conversionService;
    }

    @Override
    public void saveEnquiryPeriod(EnquiryPeriodDto dataDto) {
        Objects.requireNonNull(dataDto);
        EnquiryPeriodEntity enquiryPeriodEntity = conversionService.convert(dataDto, EnquiryPeriodEntity.class);
        enquiryPeriodRepository.save(enquiryPeriodEntity);
    }

    @Override
    public EnquiryPeriodDto calculateAndSaveEnquiryPeriod(PeriodDataDto dataDto) {
        Objects.requireNonNull(dataDto);
        String offsetValue = rulesRepository.getValue(dataDto.getCountry(),
                                                      dataDto.getProcurementMethodDetails(),
                                                      "offset");
        Long offset = Long.valueOf(offsetValue);

        String intervalValue = rulesRepository.getValue(dataDto.getCountry(),
                                                        dataDto.getProcurementMethodDetails(),
                                                        "interval");
        Long interval = Long.valueOf(intervalValue);

        TenderPeriodDto tenderPeriod = dataDto.getTenderPeriod();

        LocalDateTime enquiryPeriodEndDate = tenderPeriod.getEndDate()
                                                         .minusDays(offset);
        EnquiryPeriodDto enquiryPeriodDto = new EnquiryPeriodDto(dataDto.getOcId(), tenderPeriod.getStartDate(),
                                                                 enquiryPeriodEndDate);
        if (checkInterval(tenderPeriod.getStartDate(), enquiryPeriodEndDate, interval)) {
            EnquiryPeriodEntity enquiryPeriodEntity = conversionService.convert(enquiryPeriodDto, EnquiryPeriodEntity
                .class);
            enquiryPeriodRepository.save(enquiryPeriodEntity);
        }
        return enquiryPeriodDto;
    }

    private Boolean checkInterval(LocalDateTime startDate, LocalDateTime endDate, Long interval) {
        Long days = DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (days < interval) throw new PeriodException("Period invalid.");
        else return true;
    }
}
