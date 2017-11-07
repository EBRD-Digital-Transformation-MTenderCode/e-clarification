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
        rulesEntity.setIso("UA");
        rulesEntity.setOffset(7);
        rulesRepository = mock(RulesRepository.class);
        given(rulesRepository.getByIso(rulesEntity.getIso())).willReturn(rulesEntity);
    }

    @Test
    public void getByIso() {
        RulesEntity result = rulesRepository.getByIso(rulesEntity.getIso());
        assertEquals(result.getOffset(), rulesEntity.getOffset());
    }
}