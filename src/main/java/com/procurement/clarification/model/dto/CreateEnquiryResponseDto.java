package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.clarification.model.dto.ocds.Enquiry;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "token",
        "enquiry"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateEnquiryResponseDto {

    @JsonProperty("token")
    private final String token;

    @NotNull
    @JsonProperty("enquiry")
    private final Enquiry enquiry;

    @JsonCreator
    public CreateEnquiryResponseDto(@JsonProperty("token") final String token,
                                    @JsonProperty("enquiry") final Enquiry enquiry) {
        this.token = token;
        this.enquiry = enquiry;
    }
}

