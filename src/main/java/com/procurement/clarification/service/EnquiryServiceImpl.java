package com.procurement.clarification.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.clarification.exception.ErrorException;
import com.procurement.clarification.model.dto.*;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.dto.params.CreateEnquiryParams;
import com.procurement.clarification.model.dto.params.UpdateEnquiryParams;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.PeriodEntity;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.utils.DateUtil;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private EnquiryRepository enquiryRepository;
    private PeriodService periodService;
    private JsonUtil jsonUtil;
    private DateUtil dateUtil;

    public EnquiryServiceImpl(final EnquiryRepository enquiryRepository,
                              final PeriodService periodService,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.enquiryRepository = enquiryRepository;
        this.periodService = periodService;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto saveEnquiry(final CreateEnquiryParams params) {
        periodService.checkDateInPeriod(params.getDate(), params.getCpId(), params.getStage());
        final EnquiryDto enquiryDto = params.getDataDto().getEnquiry();
        enquiryDto.setId(UUIDs.timeBased().toString());
        enquiryDto.setDate(params.getDate());
        processAuthor(enquiryDto);
        final EnquiryEntity entity = new EnquiryEntity();
        entity.setCpId(params.getCpId());
        entity.setToken(UUIDs.timeBased());
        entity.setStage(params.getStage());
        entity.setIsAnswered(false);
        entity.setOwner(params.getOwner());
        entity.setJsonData(jsonUtil.toJson(enquiryDto));
        enquiryRepository.save(entity);
        return new ResponseDto<>(true, null,
                new CreateEnquiryResponseDto(entity.getToken().toString(), enquiryDto));
    }

    @Override
    public ResponseDto updateEnquiry(final UpdateEnquiryParams params) {
        final EnquiryEntity entity = Optional.ofNullable(
                enquiryRepository.getByCpIdAndStageAndToken(
                        params.getCpId(),
                        params.getStage(),
                        UUID.fromString(params.getToken())))
                .orElseThrow(() -> new ErrorException("Enquiry not found."));
        checkEnquiry(entity, params);
        final EnquiryDto enquiryDto = jsonUtil.toObject(EnquiryDto.class, entity.getJsonData());
        enquiryDto.setAnswer(params.getDataDto().getEnquiry().getAnswer());
        enquiryDto.setDateAnswered(params.getDate());
        entity.setIsAnswered(true);
        entity.setJsonData(jsonUtil.toJson(enquiryDto));
        enquiryRepository.save(entity);
        final Boolean allAnswered = checkAllAnsweredAfterEndPeriod(params.getCpId(), params.getStage(), params.getDate());
        return new ResponseDto<>(true, null, new UpdateEnquiryResponseDto(allAnswered, enquiryDto));
    }

    @Override
    public ResponseDto checkEnquiries(final String cpId, final String stage) {
        final PeriodEntity periodEntity = periodService.getPeriod(cpId, stage);
        final LocalDateTime endDate = periodEntity.getEndDate();
        if (dateUtil.localNowUTC().isBefore(endDate)) {
            return new ResponseDto<>(true, null, new CheckEnquiresResponseDto(null, endDate));
        } else {
            return new ResponseDto<>(true, null,
                    new CheckEnquiresResponseDto(checkAllAnswered(cpId, stage), null));
        }
    }

    private void processAuthor(final EnquiryDto enquiryDto) {
        final OrganizationReferenceDto author = enquiryDto.getAuthor();
        if (Objects.nonNull(author))
            author.setId(author.getIdentifier().getScheme() + "-" + author.getIdentifier().getId());
    }

    private void checkEnquiry(final EnquiryEntity entity, final UpdateEnquiryParams params) {
        if (!entity.getOwner().equals(params.getOwner())) {
            throw new ErrorException("Invalid owner.");
        }
        if (entity.getIsAnswered()) {
            throw new ErrorException("The enquiry already has an answer.");
        }
    }

    private Boolean checkAllAnswered(final String cpId, final String stage) {
        return (enquiryRepository.getCountOfUnanswered(cpId, stage) == 0) ? true : false;
    }

    private Boolean checkAllAnsweredAfterEndPeriod(final String cpId, final String stage, final LocalDateTime dateTime) {
        final PeriodEntity periodEntity = periodService.getPeriod(cpId, stage);
        final LocalDateTime endDate = periodEntity.getEndDate();
        if (dateTime.isAfter(endDate)) {
            return checkAllAnswered(cpId, stage);
        } else {
            return false;
        }
    }

}
