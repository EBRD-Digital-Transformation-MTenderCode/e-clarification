package com.procurement.clarification.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateAnswerRQ {

    private final UpdateAnswerRQDto dataDto;
    private final String cpId;
    private final String token;
    private final LocalDateTime date;
    private final String idPlatform;

    public UpdateAnswerRQ(final UpdateAnswerRQDto dataDto,
                          final String token,
                          final String cpId,
                          final LocalDateTime date,
                          final String idPlatform) {
        this.dataDto = dataDto;
        this.token = token;
        this.cpId=cpId;
        this.date = date;
        this.idPlatform = idPlatform;
    }
}
