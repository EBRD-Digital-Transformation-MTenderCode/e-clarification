package com.procurement.clarification.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.clarification.exception.ErrorInsertException;
import com.procurement.clarification.model.dto.*;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.PeriodEntity;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.utils.DateUtil;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private EnquiryRepository enquiryRepository;
    private PeriodService periodService;
    private JsonUtil jsonUtil;
    private DateUtil dateUtil;
    private ConversionService conversionService;

    public EnquiryServiceImpl(final EnquiryRepository enquiryRepository,
                              final PeriodService periodService,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil,
                              final ConversionService conversionService) {
        this.enquiryRepository = enquiryRepository;
        this.periodService = periodService;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseDto saveEnquiry(final CreateEnquiryParams params) {
        periodService.checkDateInPeriod(params.getDate(), params.getCpid());
        final EnquiryDto enquiryDto = params.getDataDto().getEnquiry();
        enquiryDto.setId(UUIDs.timeBased().toString());
        enquiryDto.setDate(params.getDate());
        final EnquiryEntity entity = new EnquiryEntity();
        entity.setCpId(params.getCpid());
        entity.setToken(UUIDs.timeBased().toString());
        entity.setIsAnswered(false);
        entity.setOwner(params.getOwner());
        entity.setJsonData(jsonUtil.toJson(enquiryDto));
        enquiryRepository.save(entity);
        return new ResponseDto(true, null, new CreateEnquiryResponseDto(entity.getToken(), enquiryDto));
    }

    @Override
    public ResponseDto updateEnquiry(final UpdateEnquiryParams params) {
        final Optional<EnquiryEntity> entityOptional = enquiryRepository.getByCpIdaAndToken(
                params.getCpId(),
                params.getToken());
        if (entityOptional.isPresent()) {
            final EnquiryEntity entity = entityOptional.get();
            checkEnquiry(entity, params);
            final EnquiryDto enquiryDto = jsonUtil.toObject(EnquiryDto.class, entity.getJsonData());
            enquiryDto.setAnswer(params.getDataDto().getEnquiry().getAnswer());
            entity.setIsAnswered(true);
            entity.setJsonData(jsonUtil.toJson(enquiryDto));
            enquiryRepository.save(entity);
            final Boolean isAllAnswered = isAllAnsweredAfterEndPeriod(params.getCpId());
            return new ResponseDto(true, null, new UpdateEnquiryResponseDto(isAllAnswered, enquiryDto));
        } else {
            throw new ErrorInsertException("Enquiry not found.");
        }
    }

    @Override
    public ResponseDto checkEnquiries(final String cpId, final String stage) {
        final PeriodEntity periodEntity = periodService.getPeriod(cpId);
        final LocalDateTime endDate = periodEntity.getEndDate();
        if (dateUtil.localNowUTC().isBefore(endDate)) {
            return new ResponseDto(true, null, new CheckEnquiresResponseDto(endDate, null));
        } else {
            return new ResponseDto(true, null, new CheckEnquiresResponseDto(null, isAllAnswered(cpId)));
        }
    }

    private void checkEnquiry(final EnquiryEntity entity, final UpdateEnquiryParams params) {
        if (!entity.getOwner().equals(params.getOwner())) {
            throw new ErrorInsertException("Invalid owner.");
        }
        if (entity.getIsAnswered()) {
            throw new ErrorInsertException("The enquiry already has an answer.");
        }
    }

    private Boolean isAllAnswered(final String cpId) {
        return (enquiryRepository.getCountByCpIdAndIsAnswered(cpId) == 0) ? true : false;
    }

    private Boolean isAllAnsweredAfterEndPeriod(final String cpId) {
        final PeriodEntity periodEntity = periodService.getPeriod(cpId);
        final LocalDateTime endDate = periodEntity.getEndDate();
        if (dateUtil.localNowUTC().isAfter(endDate)) {
            return isAllAnswered(cpId);
        } else {
            return false;
        }
    }

}
