package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Enquiry {
    @JsonProperty("id")
    private final String id;
    @JsonProperty("date")
    private final String dateOutput;
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

    private final LocalDateTime date;

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

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        this.dateOutput = dateSerialization(date);
        //this.dateOutput =date.format(formatter);
    }

    private String dateSerialization(LocalDateTime date) {
        String outputString = "";
        outputString += String.valueOf(date.getYear());
        outputString +="-";
        outputString+= String.valueOf(date.getMonthValue());
        outputString+="-";
        outputString+=String.valueOf(date.getDayOfMonth());
        outputString+="-T";
        outputString+=String.valueOf(date.getHour());
        outputString+=":";
        outputString+=String.valueOf(date.getMinute());
        outputString+=":";
        outputString+=String.valueOf(date.getSecond());
        outputString+="Z";

        return outputString;
    }
}