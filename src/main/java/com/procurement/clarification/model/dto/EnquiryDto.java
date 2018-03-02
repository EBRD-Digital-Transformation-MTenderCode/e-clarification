package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.LocalDateTimeDeserializer;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "date",
        "author",
        "title",
        "description",
        "answer",
        "dateAnswered",
        "relatedItem",
        "relatedLot"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnquiryDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    @JsonProperty("author")
    @NotNull
    @Valid
    private final OrganizationReferenceDto author;
    @JsonProperty("title")
    @NotNull
    @Size(min = 1, max = 100)
    private final String title;
    @JsonProperty("description")
    @NotNull
    @Size(min = 1, max = 2500)
    private final String description;
    @JsonProperty("answer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;
    @JsonProperty("dateAnswered")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date the answer to the question was provided.")
    private LocalDateTime dateAnswered;
    @JsonProperty("relatedItem")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedItem;
    @JsonProperty("relatedLot")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedLot;

    @JsonCreator
    public EnquiryDto(
            @JsonProperty("id") final String id,
            @JsonProperty("date") final LocalDateTime date,
            @JsonProperty("author") final OrganizationReferenceDto author,
            @JsonProperty("title") final String title,
            @JsonProperty("description") final String description,
            @JsonProperty("dateAnswered") final LocalDateTime dateAnswered,
            @JsonProperty("relatedItem") final String relatedItem,
            @JsonProperty("relatedLot") final String relatedLot,
            @JsonProperty("answer") final String answer) {

        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.dateAnswered = dateAnswered;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;

    }
}
