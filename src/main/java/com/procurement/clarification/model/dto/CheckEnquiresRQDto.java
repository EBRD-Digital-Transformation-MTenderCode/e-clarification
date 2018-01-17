package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(
    "tenderPeriodEndDate")
public class CheckEnquiresRQDto {

    @JsonProperty("tenderPeriodEndDate")
    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    final LocalDateTime tenderPeriodEndDate;

    public CheckEnquiresRQDto(@JsonProperty("tenderPeriodEndDate")
                                  LocalDateTime tenderPeriodEndDate) {
        this.tenderPeriodEndDate = tenderPeriodEndDate;
    }
}
