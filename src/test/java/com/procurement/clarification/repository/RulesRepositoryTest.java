package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.RulesEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RulesRepositoryTest {
    private static RulesRepository rulesRepository;

    private static RulesEntity rulesEntity;

    @BeforeAll
    static void setUp() {
        rulesEntity = new RulesEntity();
        rulesEntity.setCountry("UA");
        rulesEntity.setMethod("micro");
        rulesEntity.setParameter("offset");
        rulesEntity.setValue("3");
        rulesRepository = mock(RulesRepository.class);
        given(rulesRepository.getRule(rulesEntity.getCountry(), rulesEntity.getMethod(), rulesEntity.getParameter()))
            .willReturn(rulesEntity);
        given(rulesRepository.getValue(rulesEntity.getCountry(), rulesEntity.getMethod(), rulesEntity.getParameter())
        ).willReturn(rulesEntity.getValue());
    }

    @Test
    public void getRuleTest() {
        RulesEntity result = rulesRepository.getRule(rulesEntity.getCountry(), rulesEntity.getMethod(), rulesEntity
            .getParameter());
        assertEquals(result.getValue(), rulesEntity.getValue());
    }

    @Test
    public void getValueTest() {
        String value = rulesRepository.getValue(rulesEntity.getCountry(), rulesEntity.getMethod(), rulesEntity
            .getParameter());
        assertEquals(value, rulesEntity.getValue());
    }
}