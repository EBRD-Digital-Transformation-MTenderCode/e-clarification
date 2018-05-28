package com.procurement.clarification.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.clarification.model.dto.CheckEnquiresResponseDto;
import com.procurement.clarification.model.dto.CreateEnquiryResponseDto;
import com.procurement.clarification.model.dto.UpdateEnquiryResponseDto;
import com.procurement.clarification.model.dto.ocds.Enquiry;
import com.procurement.clarification.model.dto.ocds.OrganizationReference;
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
    public ResponseDto createEnquiry(final CreateEnquiryParams params) {
        periodService.checkDateInPeriod(params.getDate(), params.getCpId(), params.getStage());
        final Enquiry enquiryDto = params.getDataDto().getEnquiry();
        enquiryDto.setId(UUIDs.timeBased().toString());
        enquiryDto.setDate(params.getDate());
        processAuthor(enquiryDto);
        final EnquiryEntity entity = new EnquiryEntity();
        entity.setCpId(params.getCpId());
        entity.setToken(UUIDs.random());
        entity.setStage(params.getStage());
        entity.setIsAnswered(false);
        entity.setOwner(params.getOwner());
        entity.setJsonData(jsonUtil.toJson(enquiryDto));
        enquiryRepository.save(entity);
        return new ResponseDto<>(true, null,
                new CreateEnquiryResponseDto(entity.getToken().toString(), enquiryDto));
    }

    @Override
    public ResponseDto createAnswer(final UpdateEnquiryParams params) {
        final EnquiryEntity entity = Optional.ofNullable(
                enquiryRepository.getByCpIdAndStageAndToken(
                        params.getCpId(),
                        params.getStage(),
                        UUID.fromString(params.getToken())))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        checkEnquiry(entity, params);
        final Enquiry enquiryDto = jsonUtil.toObject(Enquiry.class, entity.getJsonData());
        if (!enquiryDto.getId().equals(params.getDataDto().getEnquiry().getId()))
            throw new ErrorException(ErrorType.INVALID_ID);
        enquiryDto.setAnswer(params.getDataDto().getEnquiry().getAnswer());
        enquiryDto.setDate(params.getDate());
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
        final LocalDateTime tenderEndDate = periodEntity.getTenderEndDate();
        if (dateUtil.nowUTCLocalDateTime().isAfter(tenderEndDate)) {
            return new ResponseDto<>(true, null, new CheckEnquiresResponseDto(checkAllAnswered(cpId, stage), null));
        } else {
            return new ResponseDto<>(true, null, new CheckEnquiresResponseDto(null, tenderEndDate));
        }
    }

    private void processAuthor(final Enquiry enquiryDto) {
        final OrganizationReference author = enquiryDto.getAuthor();
        if (Objects.nonNull(author))
            author.setId(author.getIdentifier().getScheme() + "-" + author.getIdentifier().getId());
    }

    private void checkEnquiry(final EnquiryEntity entity, final UpdateEnquiryParams params) {
        if (!entity.getOwner().equals(params.getOwner())) {
            throw new ErrorException(ErrorType.INVALID_OWNER);
        }
        if (entity.getIsAnswered()) {
            throw new ErrorException(ErrorType.ALREADY_HAS_ANSWER);
        }
    }

    private Boolean checkAllAnswered(final String cpId, final String stage) {
        return enquiryRepository.getCountOfUnanswered(cpId, stage) == 0;
    }

    private Boolean checkAllAnsweredAfterEndPeriod(final String cpId, final String stage, final LocalDateTime dateTime) {
        final PeriodEntity periodEntity = periodService.getPeriod(cpId, stage);
        final LocalDateTime tenderEndDate = periodEntity.getTenderEndDate();
        if (dateTime.isAfter(tenderEndDate)) {
            return checkAllAnswered(cpId, stage);
        } else {
            return false;
        }
    }

}
