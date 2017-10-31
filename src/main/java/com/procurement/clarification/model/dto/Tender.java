package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class Tender {
    @JsonProperty("enquiries")
    private final List<Enquiry> enquiries;

    @JsonCreator
    public Tender(@JsonProperty("enquiries") final List<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }
}