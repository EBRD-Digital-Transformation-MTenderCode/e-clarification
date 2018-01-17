package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
    "allAnswers",
    "enquiry"
})
public class UpdateAnswerRSDto {

    @JsonProperty("allAnswers")
    private final Boolean allAnswers;

    @NotNull
    @JsonProperty("enquiry")
    private final UpdateEnquiryRSDto enquiry;

    @JsonCreator
    public UpdateAnswerRSDto(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("allAnswers") final Boolean allAnswers,
        @NotEmpty
        @Valid
        @JsonProperty("enquiry") final UpdateEnquiryRSDto enquiry) {
        this.allAnswers=allAnswers;
        this.enquiry = enquiry;
    }
}
