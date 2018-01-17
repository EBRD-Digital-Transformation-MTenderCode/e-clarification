package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
    "id",
    "answer"
})
public class UpdateEnquiryRQDto {

    @JsonProperty("answer")
    @NotNull
    private final String answer;
    @JsonProperty("id")
    @NotNull
    private String id;

    @JsonCreator
    public UpdateEnquiryRQDto(@JsonProperty("id") final String id,
                              @JsonProperty("answer") final String answer) {

        this.id = id;
        this.answer = answer;
    }
}
