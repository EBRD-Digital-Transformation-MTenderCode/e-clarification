package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.serialization.JsonLocalDateSerializer;
import java.time.LocalDateTime;

public class Enquiry {
    @JsonProperty("id")
    private final String id;
    @JsonProperty("date")
    @JsonSerialize(using=JsonLocalDateSerializer.class)
    private final LocalDateTime date;
    @JsonProperty("author")
    private final Author author;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("answer")
    private final Answer answer;
    @JsonProperty("relatedItem")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedItem;//-
    @JsonProperty("relatedLot")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedLot;//-
    @JsonProperty("threadID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String threadID;//-


    @JsonCreator
    public Enquiry(@JsonProperty("id") final String id,

                   @JsonProperty("date") final LocalDateTime date,
                   @JsonProperty("author") final Author author,
                   @JsonProperty("title") final String title,
                   @JsonProperty("description") final String description,
                   @JsonProperty("answer") final Answer answer,
                   @JsonProperty("relatedItem") final String relatedItem,
                   @JsonProperty("relatedLot") final String relatedLot,
                   @JsonProperty("threadID") final String threadID) {

        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
        this.threadID = threadID;

    }




}