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
    "token",
    "enquiry"
})
public class CreateAnswerRSDto {

    @JsonProperty("token")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String token;

    @NotNull
    @JsonProperty("enquiry")
    private final CreateEnquiryRSDto enquiry;

    @JsonCreator
    public CreateAnswerRSDto(
        @NotNull
        @JsonProperty("token") final String token,
        @NotEmpty
        @Valid
        @JsonProperty("enquiry") final CreateEnquiryRSDto enquiry) {
        this.token = token;
        this.enquiry = enquiry;
    }
}
