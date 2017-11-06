package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.deserialization.JsonLocalDateDeserializer;
import com.procurement.clarification.databind.serialization.JsonLocalDateSerializer;
import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EnquiryDto {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("date")
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    private final LocalDateTime date;

    @JsonProperty("author")
    @NotEmpty
    private final AuthorDto author;

    @JsonProperty("title")
    @NotNull
    @Size(min=1, max=100)
    private final String title;

    @JsonProperty("description")
    @NotNull
    @Size(min=1, max=2500)
    private final String description;

    @JsonProperty("answer")
    @Max(2500)
    private final String answer;

    @JsonProperty("dateAnswered")
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
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