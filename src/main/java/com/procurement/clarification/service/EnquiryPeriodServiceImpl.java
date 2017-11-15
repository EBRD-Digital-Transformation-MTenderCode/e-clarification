package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.PeriodDataDto;
import com.procurement.clarification.model.dto.TenderPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.RulesRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class EnquiryPeriodServiceImpl implements EnquiryPeriodService {

    private EnquiryPeriodRepository enquiryPeriodRepository;
    private RulesRepository rulesRepository;

    public EnquiryPeriodServiceImpl(final EnquiryPeriodRepository enquiryPeriodRepository,
                                    final RulesRepository rulesRepository) {
        this.enquiryPeriodRepository = enquiryPeriodRepository;
        this.rulesRepository = rulesRepository;
    }

    @Override
    public void saveEnquiryPeriod(final EnquiryPeriodDto dataDto) {
        Objects.requireNonNull(dataDto);
        createEntity(dataDto.getOcId(), dataDto.getStartDate(),
                     dataDto.getEndDate()).ifPresent(s ->
                                                         enquiryPeriodRepository.save(s));
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
            createEntity(dataDto.getOcId(), tenderPeriod.getStartDate(), enquiryPeriodEndDate)
                .ifPresent(s -> enquiryPeriodRepository.save(s));
        }
    }

    private Boolean checkInterval(final LocalDateTime startDate, final LocalDateTime endDate, final Long interval) {
        final Long days = DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        return days >= interval;
    }

    public Optional<EnquiryPeriodEntity> createEntity(final String ocId,
                                                      final LocalDateTime startDate,
                                                      final LocalDateTime endDate) {
        Objects.requireNonNull(ocId);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        final EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setOcId(ocId);
        enquiryPeriodEntity.setStartDate(startDate);
        enquiryPeriodEntity.setEndDate(endDate);
        return Optional.of(enquiryPeriodEntity);
    }
}
