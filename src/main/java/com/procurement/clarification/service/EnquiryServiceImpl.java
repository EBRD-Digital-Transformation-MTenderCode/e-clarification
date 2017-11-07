package com.procurement.clarification.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.clarification.exception.ErrorInsertException;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private EnquiryRepository enquiryRepository;
    private EnquiryPeriodRepository periodRepository;
    private JsonUtil jsonUtil;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository,
                              EnquiryPeriodRepository periodRepository,
                              JsonUtil jsonUtil) {
        this.enquiryRepository = enquiryRepository;
        this.periodRepository = periodRepository;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void insertData(DataDto dataDto) {
        Objects.requireNonNull(dataDto);
        final LocalDateTime localDateTime = LocalDateTime.now();
        checkPeriod(localDateTime, dataDto.getOcid());
        convertDtoToEntity(dataDto.getOcid(), localDateTime, dataDto.getEnquiry())
            .ifPresent(enquiry -> enquiryRepository.save(enquiry));
    }

    private void checkPeriod(final LocalDateTime localDateTime, final String ocid) {
        final EnquiryPeriodEntity periodEntity = periodRepository.getByOcId(ocid);
        boolean localDateTimeAfter = localDateTime.isAfter(periodEntity.getStartDate());
        boolean localDateTimeBefore = localDateTime.isBefore(periodEntity.getEndDate());
        if (!localDateTimeAfter && !localDateTimeBefore) {
            throw new ErrorInsertException("Date not in period.");
        }
    }

    private Optional<EnquiryEntity> convertDtoToEntity(String ocId, LocalDateTime localDateTime, EnquiryDto enquiryDto) {
        EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setOcId(ocId);
        UUID enquiryId;
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
        return Optional.of(enquiryEntity);
    }
}
