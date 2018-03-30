package com.procurement.clarification.model.dto.params;

import com.procurement.clarification.model.dto.CreateEnquiryDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEnquiryParams {

    private final String cpId;
    private final String stage;
    private final String owner;
    private final CreateEnquiryDto dataDto;
    private LocalDateTime date;

    public CreateEnquiryParams(final String cpId,
                               final String stage,
                               final LocalDateTime date,
                               final String owner,
                               final CreateEnquiryDto dataDto) {
        this.cpId = cpId;
        this.stage = stage;
        this.date = date;
        this.owner = owner;
        this.dataDto = dataDto;
    }
}
