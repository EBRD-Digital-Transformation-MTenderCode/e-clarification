package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.*;
import com.procurement.clarification.model.dto.bpe.ResponseDetailsDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.PeriodEntity;
import com.procurement.clarification.repository.EnquiryRepository;
import com.procurement.clarification.repository.PeriodRepository;
import com.procurement.clarification.utils.DateUtil;
import com.procurement.clarification.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private EnquiryRepository enquiryRepository;
    private PeriodRepository periodRepository;
    private JsonUtil jsonUtil;
    private DateUtil dateUtil;
    private ConversionService conversionService;

    public EnquiryServiceImpl(final EnquiryRepository enquiryRepository,
                              final PeriodRepository periodRepository,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil,
                              final ConversionService conversionService) {
        this.enquiryRepository = enquiryRepository;
        this.periodRepository = periodRepository;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseDto saveEnquiry(final CreateAnswerRQ createAnswerRQ) {
        final LocalDateTime localDateTime = createAnswerRQ.getDate();
        checkPeriod(localDateTime, createAnswerRQ.getCpid());
        final EnquiryEntity enquiryEntity = conversionService.convert(createAnswerRQ, EnquiryEntity.class);

        final String errorMessage = checkPeriod(localDateTime, createAnswerRQ.getCpid());

        if (errorMessage != null) {
            final List<ResponseDetailsDto> responseDetails = new ArrayList<>();
            responseDetails.add(new ResponseDetailsDto("code", errorMessage));
            return new ResponseDto(false, responseDetails, null);
        } else {

            final String ownerId = periodRepository.getByCpId(createAnswerRQ.getCpid())
                    .getOwner();
            enquiryEntity.setOwner(ownerId);

            final CreateEnquiryRQDto createEnquiryRQDto = createAnswerRQ.getDataDto()
                    .getEnquiry();

            final CreateEnquiryRSDto createEnquiryRSDto = new CreateEnquiryRSDto(enquiryEntity.getEnquiryId()
                    .toString(),
                    createAnswerRQ
                            .getDate(),
                    createEnquiryRQDto.getAuthor(),
                    createEnquiryRQDto.getTitle(),
                    createEnquiryRQDto.getDescription(),
                    enquiryEntity.getIsAnswered(),
                    createEnquiryRQDto.getRelatedItem(),
                    createEnquiryRQDto.getRelatedLot());
            enquiryEntity.setJsonData(jsonUtil.toJson(createEnquiryRSDto));

            enquiryRepository.save(enquiryEntity);

            final CreateAnswerRSDto createAnswerRSDto = new CreateAnswerRSDto(enquiryEntity.getEnquiryId()
                    .toString(),
                    createEnquiryRSDto);

            return new ResponseDto(true, null, createAnswerRSDto);
        }
    }

    @Override
    public ResponseDto updateEnquiry(final UpdateAnswerRQ updateAnswerRQ) {
        final EnquiryEntity enquiryEntity = enquiryRepository.getByCpIdaAndEnquiryId(updateAnswerRQ.getCpId(), UUID
                .fromString(updateAnswerRQ.getDataDto()
                        .getEnquiry()
                        .getId()));
        if (enquiryEntity != null) {
            if (enquiryEntity.getOwner()
                    .equals(updateAnswerRQ.getIdPlatform())) {
                if (enquiryEntity.getIsAnswered()) {
                    final List<ResponseDetailsDto> responseDetails = new ArrayList<>();
                    responseDetails.add(new ResponseDetailsDto("code", "already answered"));
                    return new ResponseDto(false, responseDetails, null);
                } else {
                    final LocalDateTime currentLocalDateTime = dateUtil.localNowUTC();

                    final CreateEnquiryRSDto enquiry = jsonUtil.toObject(CreateEnquiryRSDto.class, enquiryEntity
                            .getJsonData());
                    final UpdateEnquiryRQDto answer = updateAnswerRQ.getDataDto()
                            .getEnquiry();
                    final UpdateEnquiryRSDto updateEnquiryRSDto = new UpdateEnquiryRSDto(enquiryEntity.getEnquiryId()
                            .toString(),
                            enquiry.getDate(),
                            enquiry.getAuthor(),
                            enquiry.getTitle(),
                            enquiry.getDescription(),
                            answer.getAnswer(),
                            currentLocalDateTime,
                            enquiry.getRelatedItem(),
                            enquiry.getRelatedLot(),
                            true);
                    final UpdateAnswerRSDto updateAnswerRSDto = new UpdateAnswerRSDto(null,
                            updateEnquiryRSDto);
                    enquiryEntity.setIsAnswered(true);
                    enquiryEntity.setJsonData(jsonUtil.toJson(updateAnswerRSDto));
                    enquiryRepository.save(enquiryEntity);
                    updateAnswerRSDto.setAllAnswers(
                            isAllAnsweredAfterEndPeriod(currentLocalDateTime, enquiryEntity.getCpId())
                    );
                    return new ResponseDto(true, null, updateAnswerRSDto);
                }
            } else {
                final List<ResponseDetailsDto> responseDetails = new ArrayList<>();
                responseDetails.add(new ResponseDetailsDto("code", "invalid platform id"));
                return new ResponseDto(false, responseDetails, null);
            }
        } else {
            final List<ResponseDetailsDto> responseDetails = new ArrayList<>();
            responseDetails.add(new ResponseDetailsDto("code", "not found"));
            return new ResponseDto(false, responseDetails, null);
        }
    }

    @Override
    public ResponseDto checkEnquiries(final String cpId, final String stage) {

        final PeriodEntity periodEntity = periodRepository.getByCpIdAndStage(cpId, stage);
        final LocalDateTime endDate = dateUtil.dateToLocal(periodEntity.getEndDate());
        final LocalDateTime currentDate = dateUtil.localNowUTC();

        if (dateUtil.getMilliUTC(currentDate) < dateUtil.getMilliUTC(endDate)) {
            return new ResponseDto(true, null, new CheckEnquiresPeriodRSDto(endDate));
        } else {

            return new ResponseDto(true, null, new CheckEnquiresAllAnswersRSDto(isAllAnswered(cpId)));
        }
    }

    private String checkPeriod(final LocalDateTime localDateTime, final String tenderId) {

        final PeriodEntity periodEntity = periodRepository.getByCpId(tenderId);

        if (periodEntity == null) {
            return "Period not found";
        } else {
            final boolean localDateTimeAfter = localDateTime.isAfter(dateUtil.dateToLocal(periodEntity.getStartDate()));
            final boolean localDateTimeBefore = localDateTime.isBefore(dateUtil.dateToLocal(periodEntity.getEndDate()));
            if (!localDateTimeAfter || !localDateTimeBefore) {
                return "Date not in period.";
            }
        }

        return null;
    }

    private Boolean isAllAnswered(final String cpId) {
        final long countNotAnswered = enquiryRepository.getCountByCpIdAndIsAnswered(cpId);
        Boolean allAnswers = false;
        if (countNotAnswered == 0) {
            allAnswers = true;
        }

        return allAnswers;
    }

    private Boolean isAllAnsweredAfterEndPeriod(final LocalDateTime localDateTimeNow, final String cpId) {
        final PeriodEntity periodEntity = periodRepository.getByCpId(cpId);
        if (periodEntity != null) {
            final LocalDateTime endDate = dateUtil.dateToLocal(periodEntity.getEndDate());
            if (localDateTimeNow.isAfter(endDate)) {
                return isAllAnswered(cpId);
            } else {
                return false;
            }
        }
        return null;
    }
}
