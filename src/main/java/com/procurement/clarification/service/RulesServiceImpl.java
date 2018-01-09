package com.procurement.clarification.service;

import com.procurement.clarification.exception.PeriodException;
import com.procurement.clarification.repository.RulesRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RulesServiceImpl implements RulesService {
    private static final String PARAMETER_OFFSET = "offset";
    private static final String PARAMETER_INTERVAL = "interval";

    private RulesRepository rulesRepository;

    public RulesServiceImpl(final RulesRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
    }

    @Override
    public int getOffset(final String country, final String method) {
        return Optional.ofNullable(rulesRepository.getValue(country, method, PARAMETER_OFFSET))
                .map(Integer::parseInt)
                .orElseThrow(() -> new PeriodException(getErrorMessage(country, method, PARAMETER_OFFSET)));
    }

    @Override
    public int getInterval(final String country, final String method) {
        return Optional.ofNullable(rulesRepository.getValue(country, method, PARAMETER_INTERVAL))
                .map(Integer::parseInt)
                .orElseThrow(() -> new PeriodException(getErrorMessage(country, method, PARAMETER_INTERVAL)));
    }

    private String getErrorMessage(final String country, final String method, final String param) {
        return "We don't have rules with country: " + country + ", method: " + method + ", parameter: " + param;
    }
}
