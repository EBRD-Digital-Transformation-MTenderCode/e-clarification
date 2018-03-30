package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.procurement.clarification.model.dto.ocds.Enquiry;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateEnquiryDto {

    @Valid
    @NotNull
    @JsonProperty("enquiry")
    private final Enquiry enquiry;

    @JsonCreator
    public CreateEnquiryDto(@JsonProperty("enquiry") final Enquiry enquiry) {
        this.enquiry = enquiry;
    }
}
