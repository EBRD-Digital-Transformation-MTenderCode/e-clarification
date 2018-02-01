package com.procurement.clarification.model.dto.params;

import com.procurement.clarification.model.dto.UpdateEnquiryDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEnquiryParams {

    private final String cpId;
    private final String stage;
    private final String token;
    private final LocalDateTime date;
    private final String owner;
    private final UpdateEnquiryDto dataDto;

    public UpdateEnquiryParams(final String cpId,
                               final String stage,
                               final String token,
                               final LocalDateTime date,
                               final String owner,
                               final UpdateEnquiryDto dataDto) {
        this.cpId = cpId;
        this.stage = stage;
        this.token = token;
        this.date = date;
        this.owner = owner;
        this.dataDto = dataDto;
    }
}
