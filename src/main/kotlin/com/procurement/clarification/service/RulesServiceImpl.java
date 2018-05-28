package com.procurement.clarification.service;

import com.procurement.clarification.repository.RulesRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RulesServiceImpl implements RulesService {

    private static final String PARAMETER_OFFSET = "offset";

    private RulesRepository rulesRepository;

    public RulesServiceImpl(final RulesRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
    }

    @Override
    public int getOffset(final String country, final String method) {
        return Optional.ofNullable(rulesRepository.getValue(country, method, PARAMETER_OFFSET))
                .map(Integer::parseInt)
                .orElseThrow(() -> new ErrorException(ErrorType.OFFSET_RULES_NOT_FOUND));
    }
}
