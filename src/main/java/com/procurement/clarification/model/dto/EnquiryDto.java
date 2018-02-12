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
    @JsonProperty("relatedItem")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedItem;
    @JsonProperty("relatedLot")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedLot;
    @JsonProperty("answer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;

    @JsonCreator
    public EnquiryDto(
            @JsonProperty("id") final String id,
            @JsonProperty("date") final LocalDateTime date,
            @JsonProperty("author") final OrganizationReferenceDto author,
            @JsonProperty("title") final String title,
            @JsonProperty("description") final String description,
            @JsonProperty("relatedItem") final String relatedItem,
            @JsonProperty("relatedLot") final String relatedLot,
            @JsonProperty("answer") final String answer) {

        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
        this.answer = answer;
    }
}
