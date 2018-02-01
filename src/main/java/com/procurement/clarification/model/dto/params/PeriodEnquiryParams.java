package com.procurement.clarification.model.dto.params;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodEnquiryParams {

    private final String cpId;
    private final String country;
    private final String pmd;
    private final String stage;
    private final String owner;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public PeriodEnquiryParams(final String cpId,
                               final String country,
                               final String pmd,
                               final String stage,
                               final String owner,
                               final LocalDateTime startDate,
                               final LocalDateTime endDate) {
        this.cpId = cpId;
        this.country = country;
        this.pmd = pmd;
        this.stage = stage;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
