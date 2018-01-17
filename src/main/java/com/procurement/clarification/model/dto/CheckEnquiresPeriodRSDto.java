package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(
    "tenderPeriodEndDate")
public class CheckEnquiresPeriodRSDto {

    @JsonProperty("tenderPeriodEndDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime tenderPeriodEndDate;

    public CheckEnquiresPeriodRSDto(@JsonProperty("tenderPeriodEndDate")
                                    @NotNull
                                    @JsonDeserialize(using = LocalDateDeserializer.class) final LocalDateTime
                                        tenderPeriodEndDate) {
        this.tenderPeriodEndDate = tenderPeriodEndDate;
    }
}
