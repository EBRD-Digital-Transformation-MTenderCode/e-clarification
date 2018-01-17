package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonPropertyOrder(
    "enquiry")
public class CreateAnswerRQDto {

    @NotNull
    @Valid
    @JsonProperty("enquiry")
    private final CreateEnquiryRQDto enquiry;

    @JsonCreator
    public CreateAnswerRQDto(
        @NotEmpty
        @Valid
        @JsonProperty("enquiry") final CreateEnquiryRQDto enquiry) {
        this.enquiry = enquiry;
    }
}
