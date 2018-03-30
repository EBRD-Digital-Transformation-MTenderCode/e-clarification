package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateEnquiryDto {

    @Valid
    @NotNull
    @JsonProperty("enquiry")
    private final EnquiryAnswerDto enquiry;

    @JsonCreator
    public UpdateEnquiryDto(@JsonProperty("enquiry") final EnquiryAnswerDto enquiry) {
        this.enquiry = enquiry;
    }
}
