package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.LocalDateTimeDeserializer;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
    "id",
    "answer"
})
public class UpdateEnquiryRQDto {


    @JsonProperty("id")
    @NotNull
    private String id;

    @JsonProperty("answer")
    @NotNull
    private final String answer;


    @JsonCreator
    public UpdateEnquiryRQDto(@JsonProperty("id") final String id,
                              @JsonProperty("answer") final String answer) {

        this.id = id;
        this.answer = answer;
    }
}
