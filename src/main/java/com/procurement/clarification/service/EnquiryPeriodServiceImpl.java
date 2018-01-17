package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.RulesRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public ResponseDto calculateAndSaveEnquiryPeriod(
        final String cpId,
        final String country,
        final String stage,
        final String pmd,
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final String owner) {
        final String offsetValue = rulesRepository.getValue(country,
                                                            pmd,
                                                            "offset");
        final Long offset = Long.valueOf(offsetValue);

        final String intervalValue = rulesRepository.getValue(country,
                                                              pmd,
                                                              "interval");
        final Long interval = Long.valueOf(intervalValue);

        final LocalDateTime enquiryPeriodEndDate = endDate.minusDays(offset);

        if (checkInterval(startDate, enquiryPeriodEndDate, interval)) {
            final EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();

            enquiryPeriodEntity.setCpId(cpId);
            enquiryPeriodEntity.setOwner(owner);
            enquiryPeriodEntity.setStartDate(startDate);
            enquiryPeriodEntity.setEndDate(endDate);
            enquiryPeriodEntity.setEnquiryEndDate(enquiryPeriodEndDate);
            enquiryPeriodEntity.setStage(stage);

            enquiryPeriodRepository.save(enquiryPeriodEntity);

            return new ResponseDto(true, null, new EnquiryPeriodDto(startDate, enquiryPeriodEndDate));
        } else {
            final ResponseDto.ResponseDetailsDto responseDetailsDto = new ResponseDto.ResponseDetailsDto(
                "code",
                "offset date is not in interval"
            );
            final List<ResponseDto.ResponseDetailsDto> details = new ArrayList<>();
            details.add(responseDetailsDto);

            return new ResponseDto(false, details, null);
        }
    }

    Boolean checkInterval(final LocalDateTime startDate, final LocalDateTime endDate, final Long interval) {
        final Long days = DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        return days >= interval;
    }
}
