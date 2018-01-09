package com.procurement.clarification.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.clarification.exception.ErrorInsertException;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.PeriodEntity;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.repository.PeriodRepository;
import com.procurement.clarification.utils.DateUtil;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final PeriodRepository periodRepository;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public EnquiryServiceImpl(final EnquiryRepository enquiryRepository,
                              final PeriodRepository periodRepository,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.enquiryRepository = enquiryRepository;
        this.periodRepository = periodRepository;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public void insertData(final DataDto dataDto) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        checkPeriod(localDateTime, dataDto.getOcid());
        enquiryRepository.save(getEntity(dataDto.getOcid(), localDateTime, dataDto.getEnquiry()));
    }

    private void checkPeriod(final LocalDateTime dateTime, final String ocid) {
        final PeriodEntity periodEntity = periodRepository.getByOcId(ocid);
        final boolean localDateTimeAfter = dateTime.isAfter(dateUtil.dateToLocal(periodEntity.getStartDate()));
        final boolean localDateTimeBefore = dateTime.isBefore(dateUtil.dateToLocal(periodEntity.getEndDate()));
        if (!localDateTimeAfter && !localDateTimeBefore) {
            throw new ErrorInsertException("Date not in period.");
        }
    }

    @Override
    public Boolean checkEnquiries(final String ocid) {
        return !enquiryRepository.getByOcIdNotAnswered(ocid).isPresent();
    }

    private EnquiryEntity getEntity(final String ocId,
                                    final LocalDateTime localDateTime,
                                    final EnquiryDto enquiryDto) {
        final EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setOcId(ocId);
        final UUID enquiryId;
        if (Objects.isNull(enquiryDto.getId())) {
            enquiryId = UUIDs.timeBased();
            enquiryDto.setId(enquiryId.toString());
        } else {
            enquiryId = java.util.UUID.fromString(enquiryDto.getId());
        }
        if (Objects.isNull(enquiryDto.getDate())) {
            enquiryDto.setDate(localDateTime);
        }
        if (Objects.nonNull(enquiryDto.getAnswer()) && Objects.nonNull(enquiryDto.getDateAnswered())) {
            enquiryEntity.setIsAnswered(true);
        }
        enquiryEntity.setEnquiryId(enquiryId);
        enquiryEntity.setJsonData(jsonUtil.toJson(enquiryDto));
        return enquiryEntity;
    }
}
