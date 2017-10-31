package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EnquiryPeriodServiceImpl implements EnquiryPeriodService {

    private EnquiryPeriodRepository enquiryPeriodRepository;

    public EnquiryPeriodServiceImpl(EnquiryPeriodRepository enquiryPeriodRepository) {
        this.enquiryPeriodRepository = enquiryPeriodRepository;
    }

    @Override
    public void insertData(EnquiryPeriodDto dataDto) {
        Objects.requireNonNull(dataDto);
        convertDtoToEntity(dataDto)
            .ifPresent(period -> enquiryPeriodRepository.save(period));
    }

    public Optional<EnquiryPeriodEntity> convertDtoToEntity(EnquiryPeriodDto dataDto) {
        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setTenderId(dataDto.getTenderId());
        enquiryPeriodEntity.setStartDate(dataDto.getStartDate());
        enquiryPeriodEntity.setEndDate(dataDto.getEndDate());
        return Optional.of(enquiryPeriodEntity);
    }
}
