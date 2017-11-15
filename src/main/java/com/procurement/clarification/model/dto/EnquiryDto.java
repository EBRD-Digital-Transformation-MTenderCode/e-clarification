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
    "date",
    "author",
    "title",
    "description",
    "answer",
    "dateAnswered",
    "relatedItem",
    "relatedLot",
    "threadID"
})
public class EnquiryDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    @JsonProperty("author")
    @NotEmpty
    private final AuthorDto author;

    @JsonProperty("title")
    @NotNull
    @Size(min = 1, max = 100)
    private final String title;

    @JsonProperty("description")
    @NotNull
    @Size(min = 1, max = 2500)
    private final String description;

    @JsonProperty("answer")
    @Max(2500)
    private final String answer;

    @JsonProperty("dateAnswered")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime dateAnswered;

    @JsonProperty("relatedItem")
    private final String relatedItem;

    @JsonProperty("relatedLot")
    private final String relatedLot;

    @JsonProperty("threadID")
    private final String threadID;

    @JsonCreator
    public EnquiryDto(@JsonProperty("id") final String id,
                      @JsonProperty("date") final LocalDateTime date,
                      @JsonProperty("author") final AuthorDto author,
                      @JsonProperty("title") final String title,
                      @JsonProperty("description") final String description,
                      @JsonProperty("answer") final String answer,
                      @JsonProperty("dateAnswered") final LocalDateTime dateAnswered,
                      @JsonProperty("relatedItem") final String relatedItem,
                      @JsonProperty("relatedLot") final String relatedLot,
                      @JsonProperty("threadID") final String threadID) {

        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.dateAnswered = dateAnswered;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
        this.threadID = threadID;
    }
}