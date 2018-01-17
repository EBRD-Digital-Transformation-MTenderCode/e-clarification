package com.procurement.clarification.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateAnswerRQ {
    private final String cpid;
    private final String stage;
    @Setter
    private LocalDateTime date;
    private final String idPlatform;
    private final CreateAnswerRQDto dataDto;

    public CreateAnswerRQ(final String cpid,
                          final String stage,
                          final LocalDateTime date,
                          final String idPlatform,
                          final CreateAnswerRQDto dataDto) {
        this.cpid = cpid;
        this.stage = stage;
        this.date = date;
        this.idPlatform = idPlatform;
        this.dataDto = dataDto;
    }

}
