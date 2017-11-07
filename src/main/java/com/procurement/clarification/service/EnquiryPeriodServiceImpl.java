package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.model.entity.RulesEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.RulesRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EnquiryPeriodServiceImpl implements EnquiryPeriodService {

    private EnquiryPeriodRepository enquiryPeriodRepository;
    private RulesRepository rulesRepository;

    public EnquiryPeriodServiceImpl(EnquiryPeriodRepository enquiryPeriodRepository,
                                    RulesRepository rulesRepository) {
        this.enquiryPeriodRepository = enquiryPeriodRepository;
        this.rulesRepository = rulesRepository;
    }

    @Override
    public void saveEnquiryPeriod(EnquiryPeriodDto dataDto) {
        Objects.requireNonNull(dataDto);
        convertDtoToEntity(dataDto)
            .ifPresent(period -> enquiryPeriodRepository.save(period));
    }

    @Override
    public void calculateAndSaveEnquiryPeriod(EnquiryPeriodDto dataDto, String iso) {
        Objects.requireNonNull(dataDto);
        Objects.requireNonNull(iso);
        RulesEntity rules = rulesRepository.getByIso(iso);
        dataDto.setEndDate(dataDto.getEndDate()
                                  .minusDays(rules.getOffset()));

        convertDtoToEntity(dataDto)
            .ifPresent(period -> enquiryPeriodRepository.save(period));
    }

    public Optional<EnquiryPeriodEntity> convertDtoToEntity(EnquiryPeriodDto dataDto) {
        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setOcId(dataDto.getOcId());
        enquiryPeriodEntity.setStartDate(dataDto.getStartDate());
        enquiryPeriodEntity.setEndDate(dataDto.getEndDate());
        return Optional.of(enquiryPeriodEntity);
    }
}
