package com.procurement.clarification.service;

import com.procurement.clarification.exception.PeriodException;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.entity.PeriodEntity;
import com.procurement.clarification.repository.PeriodRepository;
import com.procurement.clarification.utils.DateUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class PeriodServiceImpl implements PeriodService {

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
    public ResponseDto calculateAndSavePeriod(final String cpid,
                                              final String country,
                                              final String pmd,
                                              final String stage,
                                              final String owner,
                                              final LocalDateTime startDate,
                                              final LocalDateTime endDate) {

        final int offset = rulesService.getOffset(country, pmd);
        final int interval = rulesService.getInterval(country, pmd);
        final LocalDateTime enquiryPeriodEndDate = endDate.minusDays(offset);
        if (checkInterval(startDate, enquiryPeriodEndDate, interval)) {
            final PeriodEntity periodEntity = new PeriodEntity();
            periodEntity.setCpId(cpid);
            periodEntity.setStage(stage);
            periodEntity.setStartDate(dateUtil.localToDate(startDate));
            periodEntity.setEndDate(dateUtil.localToDate(enquiryPeriodEndDate));
            periodEntity.setOwner(owner);
            periodEntity.setTenderPeriodEndDate(dateUtil.localToDate(endDate));
            periodRepository.save(periodEntity);
        }
        return new ResponseDto(true, null, new EnquiryPeriodDto(startDate, enquiryPeriodEndDate));
    }


    @Override
    public void checkPeriod(final String cpid) {
        if (isPeriodValid(cpid)) {
            throw new PeriodException("Not found date.");
        }
    }

    @Override
    public PeriodEntity getPeriod(final String cpid) {
        final Optional<PeriodEntity> entityOptional = periodRepository.getByCpId(cpid);
        if (entityOptional.isPresent()) {
            return entityOptional.get();
        } else {
            throw new PeriodException("Period not found");
        }
    }

    public boolean isPeriodValid(final String cpid) {
        final LocalDateTime localDateTime = dateUtil.localNowUTC();
        final PeriodEntity periodEntity = getPeriod(cpid);
        final boolean localDateTimeAfter = localDateTime.isAfter(periodEntity.getStartDate());
        final boolean localDateTimeBefore = localDateTime.isBefore(periodEntity.getEndDate());
        return !localDateTimeAfter || !localDateTimeBefore;
    }

    private Boolean checkInterval(final LocalDateTime startDate, final LocalDateTime endDate, final int interval) {
        final Long days = DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (days < interval) throw new PeriodException("Period invalid.");
        else return true;
    }
}
