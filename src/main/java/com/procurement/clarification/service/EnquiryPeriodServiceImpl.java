package com.procurement.clarification.service;

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

    public EnquiryPeriodServiceImpl(final EnquiryPeriodRepository enquiryPeriodRepository,
                                    final RulesRepository rulesRepository,
                                    final ConversionService conversionService) {
        this.enquiryPeriodRepository = enquiryPeriodRepository;
        this.rulesRepository = rulesRepository;
        this.conversionService = conversionService;
    }

    @Override
    public void saveEnquiryPeriod(final EnquiryPeriodDto dataDto) {
        EnquiryPeriodEntity enquiryPeriodEntity = conversionService.convert(dataDto, EnquiryPeriodEntity.class);
        enquiryPeriodRepository.save(enquiryPeriodEntity);
    }

    @Override
    public void calculateAndSaveEnquiryPeriod(final PeriodDataDto dataDto) {
        Objects.requireNonNull(dataDto);
        final String offsetValue = rulesRepository.getValue(dataDto.getCountry(),
                                                            dataDto.getProcurementMethodDetails(),
                                                            "offset");
        final Long offset = Long.valueOf(offsetValue);

        final String intervalValue = rulesRepository.getValue(dataDto.getCountry(),
                                                              dataDto.getProcurementMethodDetails(),
                                                              "interval");
        final Long interval = Long.valueOf(intervalValue);

        final TenderPeriodDto tenderPeriod = dataDto.getTenderPeriod();

        final LocalDateTime enquiryPeriodEndDate = tenderPeriod.getEndDate()
                                                               .minusDays(offset);

        if (checkInterval(tenderPeriod.getStartDate(), enquiryPeriodEndDate, interval)) {
            EnquiryPeriodEntity enquiryPeriodEntity = conversionService.convert(dataDto, EnquiryPeriodEntity.class);
            enquiryPeriodRepository.save(enquiryPeriodEntity);
        }
    }

    private Boolean checkInterval(final LocalDateTime startDate, final LocalDateTime endDate, final Long interval) {
        final Long days = DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        return days >= interval;
    }
}
