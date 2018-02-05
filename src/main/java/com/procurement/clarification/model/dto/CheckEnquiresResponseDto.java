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
        "allAnswered",
        "tenderPeriodEndDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckEnquiresResponseDto {


    @JsonProperty("allAnswered")
    private final Boolean allAnswered;

    @JsonProperty("tenderPeriodEndDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDateTime tenderPeriodEndDate;

    public CheckEnquiresResponseDto(@JsonProperty("allAnswered") final Boolean allAnswered,
                                    @JsonProperty("tenderPeriodEndDate") final LocalDateTime tenderPeriodEndDate) {
        this.allAnswered = allAnswered;
        this.tenderPeriodEndDate = tenderPeriodEndDate;
    }
}
