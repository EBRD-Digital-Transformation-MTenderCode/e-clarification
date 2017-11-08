package com.procurement.clarification.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.repository.EnquiryRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private EnquiryRepository enquiryRepository;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    public void insertData(DataDto data) {
        Objects.requireNonNull(data);
        convertDtoToEntity(data)
            .ifPresent(period -> enquiryRepository.save(period));
    }

    public Optional<EnquiryEntity> convertDtoToEntity(DataDto dataDto) {

        EnquiryEntity enquiryEntity = new EnquiryEntity();
        enquiryEntity.setOcId(dataDto.getOcid());
        enquiryEntity.setEnquiryId(UUIDs.timeBased());
        enquiryEntity.setJsonData(writeJsonToString(dataDto));
        return Optional.of(enquiryEntity);
    }

    private String writeJsonToString(DataDto dataDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = null;

        try {
            jsonData = objectMapper.writeValueAsString(dataDto);
        } catch (JsonProcessingException e) {

        }
        return jsonData;
    }
}
