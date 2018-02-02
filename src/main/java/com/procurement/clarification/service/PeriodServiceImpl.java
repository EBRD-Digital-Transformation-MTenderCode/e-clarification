package com.procurement.clarification.service;

import com.procurement.clarification.exception.ErrorException;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.dto.params.PeriodEnquiryParams;
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
    public ResponseDto calculateAndSavePeriod(final PeriodEnquiryParams params) {
        final int offset = rulesService.getOffset(params.getCountry(), params.getPmd());
        final int interval = rulesService.getInterval(params.getCountry(), params.getPmd());
        final LocalDateTime enquiryPeriodEndDate = params.getEndDate().minusDays(offset);
        if (checkInterval(params.getStartDate(), enquiryPeriodEndDate, interval)) {
            final PeriodEntity periodEntity = new PeriodEntity();
            periodEntity.setCpId(params.getCpId());
            periodEntity.setStage(params.getStage());
            periodEntity.setOwner(params.getOwner());
            periodEntity.setStartDate(dateUtil.localToDate(params.getStartDate()));
            periodEntity.setEndDate(dateUtil.localToDate(enquiryPeriodEndDate));
            periodRepository.save(periodEntity);
        }
        return new ResponseDto<>(true, null,
                new EnquiryPeriodDto(params.getStartDate(), enquiryPeriodEndDate));
    }


    @Override
    public void checkDateInPeriod(final LocalDateTime localDateTime,
                                  final String cpId,
                                  final String stage,
                                  final String owner) {
        final LocalDateTime localDateTimeNow = dateUtil.localNowUTC();
        final PeriodEntity periodEntity = getPeriod(cpId, stage, owner);
        final boolean localDateTimeBefore = localDateTimeNow.isBefore(periodEntity.getEndDate());
        final boolean localDateTimeAfter = localDateTimeNow.isAfter(periodEntity.getStartDate());
        if (!localDateTimeBefore || !localDateTimeAfter) {
            throw new ErrorException("Date does not match period.");
        }
    }

    @Override
    public PeriodEntity getPeriod(final String cpId, final String stage, final String owner) {
        final Optional<PeriodEntity> entityOptional = periodRepository.getByCpIdAndStage(cpId, stage);
        if (entityOptional.isPresent()) {
            PeriodEntity entity = entityOptional.get();
            if (!entity.getOwner().equals(owner)) throw new ErrorException("Invalid owner.");
            return entity;
        } else {
            throw new ErrorException("Period not found");
        }
    }

    private Boolean checkInterval(final LocalDateTime startDate, final LocalDateTime endDate, final int interval) {
        final Long days = DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (days < interval) throw new ErrorException("Period invalid.");
        return true;
    }
}
