package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.CreateAnswerRQ;
import com.procurement.clarification.model.dto.CreateAnswerRSDto;
import com.procurement.clarification.model.dto.CreateEnquiryRQDto;
import com.procurement.clarification.model.dto.CreateEnquiryRSDto;
import com.procurement.clarification.model.dto.UpdateAnswerRQ;
import com.procurement.clarification.model.dto.UpdateAnswerRSDto;
import com.procurement.clarification.model.dto.UpdateEnquiryRQDto;
import com.procurement.clarification.model.dto.UpdateEnquiryRSDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.EnquiryRepository;
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
    private EnquiryPeriodRepository periodRepository;
    private JsonUtil jsonUtil;
    private DateUtil dateUtil;
    private ConversionService conversionService;

    public EnquiryServiceImpl(final EnquiryRepository enquiryRepository,
                              final EnquiryPeriodRepository periodRepository,
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

        String errorMessage = checkPeriod(localDateTime, createAnswerRQ.getCpid());

        if (errorMessage != null) {
            List<ResponseDto.ResponseDetailsDto> responseDetails = new ArrayList<>();
            responseDetails.add(new ResponseDto.ResponseDetailsDto("code", errorMessage));
            return new ResponseDto(false, responseDetails, null);
        } else {

            String ownerId = periodRepository.getByCpId(createAnswerRQ.getCpid())
                                             .getOwner();
            enquiryEntity.setOwner(ownerId);

            CreateEnquiryRQDto createEnquiryRQDto = createAnswerRQ.getDataDto()
                                                                  .getEnquiry();

            CreateEnquiryRSDto createEnquiryRSDto = new CreateEnquiryRSDto(enquiryEntity.getEnquiryId()
                                                                                        .toString(), createAnswerRQ
                                                                               .getDate(),
                                                                           createEnquiryRQDto.getAuthor(),
                                                                           createEnquiryRQDto.getTitle(),
                                                                           createEnquiryRQDto.getDescription(),
                                                                           enquiryEntity.getIsAnswered(),
                                                                           createEnquiryRQDto.getRelatedItem(),
                                                                           createEnquiryRQDto.getRelatedLot());
            enquiryEntity.setJsonData(jsonUtil.toJson(createEnquiryRSDto));

            enquiryRepository.save(enquiryEntity);

            CreateAnswerRSDto createAnswerRSDto = new CreateAnswerRSDto(enquiryEntity.getEnquiryId()
                                                                                     .toString(), createEnquiryRSDto);

            return new ResponseDto(true, null, createAnswerRSDto);
        }
    }

    @Override
    public ResponseDto updateEnquiry(UpdateAnswerRQ updateAnswerRQ) {

        EnquiryEntity enquiryEntity = enquiryRepository.getByCpIdaAndEnquiryId(updateAnswerRQ.getCpId(), UUID.fromString(updateAnswerRQ.getDataDto()
                                                                                                                                       .getEnquiry()
                                                                                                                                       .getId()));
        if (enquiryEntity != null) {

            if (enquiryEntity.getOwner()
                             .equals(updateAnswerRQ.getIdPlatform())) {
                if (enquiryEntity.getIsAnswered()) {
                    List<ResponseDto.ResponseDetailsDto> responseDetails = new ArrayList<>();
                    responseDetails.add(new ResponseDto.ResponseDetailsDto("code", "already answered"));
                    return new ResponseDto(false, responseDetails, null);
                } else {
                    CreateEnquiryRSDto enquiry = jsonUtil.toObject(CreateEnquiryRSDto.class, enquiryEntity
                        .getJsonData());

                    UpdateEnquiryRQDto answer = updateAnswerRQ.getDataDto()
                                                              .getEnquiry();

                    UpdateEnquiryRSDto updateEnquiryRSDto = new UpdateEnquiryRSDto(enquiryEntity.getEnquiryId()
                                                                                                .toString(),
                                                                                   enquiry.getDate(),
                                                                                   enquiry.getAuthor(),
                                                                                   enquiry.getTitle(),
                                                                                   enquiry.getDescription(),
                                                                                   answer.getAnswer(),
                                                                                   dateUtil.getNowUTC(),
                                                                                   enquiry.getRelatedItem(),
                                                                                   enquiry.getRelatedLot(),
                                                                                   true);

                    UpdateAnswerRSDto updateAnswerRSDto = new UpdateAnswerRSDto(null, updateEnquiryRSDto);

                    enquiryEntity.setIsAnswered(true);
                    enquiryEntity.setJsonData(jsonUtil.toJson(updateAnswerRSDto));

                    enquiryRepository.save(enquiryEntity);

                    return new ResponseDto(true, null, updateAnswerRSDto);
                }
            } else {
                List<ResponseDto.ResponseDetailsDto> responseDetails = new ArrayList<>();
                responseDetails.add(new ResponseDto.ResponseDetailsDto("code", "invalid platform id"));
                return new ResponseDto(false, responseDetails, null);
            }
        } else {
            List<ResponseDto.ResponseDetailsDto> responseDetails = new ArrayList<>();
            responseDetails.add(new ResponseDto.ResponseDetailsDto("code", "not found"));
            return new ResponseDto(false, responseDetails, null);
        }
    }

    String checkPeriod(final LocalDateTime localDateTime, final String tenderId) {

        EnquiryPeriodEntity enquiryPeriodEntity = periodRepository.getByCpId(tenderId);

        if (periodRepository.getByCpId(tenderId) == null) {
            return "Period not found";
        } else {
            final boolean localDateTimeAfter = localDateTime.isAfter(enquiryPeriodEntity.getStartDate());
            final boolean localDateTimeBefore = localDateTime.isBefore(enquiryPeriodEntity.getEndDate());
            if (!localDateTimeAfter || !localDateTimeBefore) {
                return "Date not in period.";
            }
        }

        return null;
    }
}
