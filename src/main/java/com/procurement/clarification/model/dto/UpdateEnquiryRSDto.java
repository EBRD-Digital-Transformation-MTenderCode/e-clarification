package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.LocalDateTimeDeserializer;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    "dateAnswered",
    "answer",
    "signOfResponse",
    "relatedItem",
    "relatedLot"
})
public class UpdateEnquiryRSDto {

    @JsonProperty("author")
    private final AuthorDto author;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("answer")
    private final String answer;
    @JsonProperty("dateAnswered")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime dateAnswered;
    @JsonProperty("relatedItem")
    private final String relatedItem;
    @JsonProperty("relatedLot")
    private final String relatedLot;
    @JsonProperty("signOfResponse")
    private final Boolean signOfResponse;
    @JsonProperty("id")
    private String id;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;

    @JsonCreator
    public UpdateEnquiryRSDto(
        @NotNull
        @JsonProperty("id") final String id,
        @NotNull
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonProperty("date") final LocalDateTime date,
        @NotNull
        @Valid
        @JsonProperty("author") final AuthorDto author,
        @NotNull
        @JsonProperty("title") final String title,
        @NotNull
        @JsonProperty("description") final String description,
        @NotNull
        @JsonProperty("answer") final String answer,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonProperty("dateAnswered") final LocalDateTime dateAnswered,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("relatedItem") final String relatedItem,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("relatedLot") final String relatedLot,
        @NotNull
        @JsonProperty("signOfResponse") final Boolean signOfResponse) {

        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.dateAnswered = dateAnswered;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
        this.signOfResponse = signOfResponse;
    }
}
