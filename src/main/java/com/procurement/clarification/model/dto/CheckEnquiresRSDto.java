package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(
    "allAnswers")
public class CheckEnquiresRSDto {

    @JsonProperty("allAnswers")
    final Boolean allAnswers;

    public CheckEnquiresRSDto(
        @NotNull
        @JsonProperty("allAnswers")
                                  Boolean allAnswers) {
        this.allAnswers = allAnswers;
    }
}
