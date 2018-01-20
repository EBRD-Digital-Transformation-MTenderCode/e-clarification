package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(
    "allAnswers")
public class CheckEnquiresAllAnswersRSDto {

    @JsonProperty("allAnswers")
    private final Boolean allAnswers;

    public CheckEnquiresAllAnswersRSDto(
        @NotNull
        @JsonProperty("allAnswers") final Boolean allAnswers) {
        this.allAnswers = allAnswers;
    }
}
