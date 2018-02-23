package com.procurement.clarification.service;

import com.procurement.clarification.exception.ErrorException;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.dto.params.PeriodEnquiryParams;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.PeriodEntity;
import com.procurement.clarification.repository.PeriodRepository;
import com.procurement.clarification.utils.DateUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class PeriodServiceImpl implements PeriodService {

    private static final String TEST_PARAM = "test";
    private final PeriodRepository periodRepository;
    private final RulesService rulesService;
    private final DateUtil dateUtil;

    public PeriodServiceImpl(final PeriodRepository periodRepository,
                             final RulesService rulesService,
                             final DateUtil dateUtil) {
        this.periodRepository = periodRepository;
        this.rulesService = rulesService;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto calculateAndSavePeriod(final PeriodEnquiryParams params) {
        final int offset = rulesService.getOffset(params.getCountry(), params.getPmd());
        final int interval = rulesService.getInterval(params.getCountry(), params.getPmd());
        final LocalDateTime startDate = params.getStartDate();
        final LocalDateTime endDate;
        if (TEST_PARAM.equals(params.getCountry()) && TEST_PARAM.equals(params.getPmd())) {
            endDate = params.getEndDate().minusMinutes(offset);
            final long minutes = MINUTES.between(startDate, endDate);
            if (minutes < interval) throw new ErrorException("Period invalid.");
        } else {
            endDate = params.getEndDate().minusDays(offset);
            final Long days = DAYS.between(startDate, endDate);
            if (days < interval) throw new ErrorException("Period invalid.");
        }
        final PeriodEntity periodEntity = new PeriodEntity();
        periodEntity.setCpId(params.getCpId());
        periodEntity.setStage(params.getStage());
        periodEntity.setOwner(params.getOwner());
        periodEntity.setStartDate(dateUtil.localToDate(startDate));
        periodEntity.setEndDate(dateUtil.localToDate(endDate));
        periodRepository.save(periodEntity);
        return new ResponseDto<>(true, null,
                new EnquiryPeriodDto(startDate, endDate));
    }


    @Override
    public void checkDateInPeriod(final LocalDateTime localDateTime,
                                  final String cpId,
                                  final String stage) {
        final PeriodEntity periodEntity = getPeriod(cpId, stage);
        final boolean localDateTimeBefore = localDateTime.isBefore(periodEntity.getEndDate());
        final boolean localDateTimeAfter = localDateTime.isAfter(periodEntity.getStartDate());
        if (!localDateTimeBefore || !localDateTimeAfter) {
            throw new ErrorException("Date does not match period.");
        }
    }

    @Override
    public PeriodEntity getPeriod(final String cpId, final String stage) {
        return Optional.ofNullable(
                periodRepository.getByCpIdAndStage(cpId, stage))
                .orElseThrow(() -> new ErrorException("Period not found."));
    }
}
