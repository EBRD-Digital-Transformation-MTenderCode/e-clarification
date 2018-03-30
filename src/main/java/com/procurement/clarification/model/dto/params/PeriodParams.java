package com.procurement.clarification.model.dto.params;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodParams {

    private final String cpId;
    private final String stage;
    private final String owner;
    private final String country;
    private final String pmd;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public PeriodParams(final String cpId,
                        final String stage,
                        final String owner,
                        final String country,
                        final String pmd,
                        final LocalDateTime startDate,
                        final LocalDateTime endDate) {
        this.cpId = cpId;
        this.stage = stage;
        this.owner = owner;
        this.country = country;
        this.pmd = pmd;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
