package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "tenderPeriodEndDate",
        "allAnswers"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckEnquiresResponseDto {

    @JsonProperty("tenderPeriodEndDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDateTime tenderPeriodEndDate;

    @JsonProperty("allAnswers")
    private final Boolean allAnswers;

    public CheckEnquiresResponseDto(@JsonProperty("tenderPeriodEndDate") final LocalDateTime tenderPeriodEndDate,
                                    @JsonProperty("allAnswers") final Boolean allAnswers) {
        this.tenderPeriodEndDate = tenderPeriodEndDate;
        this.allAnswers = allAnswers;
    }
}
