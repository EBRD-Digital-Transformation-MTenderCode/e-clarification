package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuthorDto {
    @JsonProperty("name")
    @NotNull
    private final String name;

    @JsonProperty("id")
    @NotNull
    private final String id;

    @JsonCreator
    public AuthorDto(@JsonProperty("name") final String name, @JsonProperty("id") final String id) {
        this.name = name;
        this.id = id;
    }
}
