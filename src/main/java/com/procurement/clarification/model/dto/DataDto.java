package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
    "ocid",
    "enquiry"
})
public class DataDto {
    @NotNull
    @JsonProperty("ocid")
    private String ocid;
    @NotNull
    @JsonProperty("enquiry")
    private EnquiryDto enquiry;

    @JsonCreator
    public DataDto(@JsonProperty("ocid") final String ocid,
                                 @JsonProperty("enquiry") final EnquiryDto enquiry) {
        this.ocid = ocid;
        this.enquiry = enquiry;
    }
}