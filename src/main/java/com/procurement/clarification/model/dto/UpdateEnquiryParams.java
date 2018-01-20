package com.procurement.clarification.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateEnquiryParams {

    private final String cpId;
    private final String token;
    private final LocalDateTime date;
    private final String owner;
    private final UpdateEnquiryDto dataDto;

    public UpdateEnquiryParams(final String token,
                               final String cpId,
                               final LocalDateTime date,
                               final String owner,
                               final UpdateEnquiryDto dataDto) {
        this.token = token;
        this.cpId = cpId;
        this.date = date;
        this.owner = owner;
        this.dataDto = dataDto;
    }
}
