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
public class UpdateAnswerRQDto {
    @NotNull
    @Valid
    @JsonProperty("enquiry")
    private final UpdateEnquiryRQDto enquiry;

    @JsonCreator
    public UpdateAnswerRQDto(

        @NotEmpty
        @Valid
        @JsonProperty("enquiry") final UpdateEnquiryRQDto enquiry) {

        this.enquiry = enquiry;
    }
}
