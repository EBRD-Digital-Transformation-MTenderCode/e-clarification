package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DataDto {
    @JsonProperty("tender")
    private final Tender tender;

    @JsonCreator
    public DataDto(@JsonProperty("tender") final Tender tender) {
        this.tender = tender;
    }
}