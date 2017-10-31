package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Author {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("id")
    private final String id;

    @JsonCreator
    public Author(@JsonProperty("name") final String name, @JsonProperty("id") final String id) {
        this.name = name;
        this.id = id;
    }
}