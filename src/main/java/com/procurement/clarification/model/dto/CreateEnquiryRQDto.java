package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
    "author",
    "title",
    "description",
    "relatedItem",
    "relatedLot"
})
public class CreateEnquiryRQDto {
    public static final int MAX_LENGTH_TITLE = 100;
    public static final int MAX_LENGHT_DESCRIPTION = 2500;
    public static final int MAX_LENGHT_ANSWER = 2500;

    @JsonProperty("author")
    @NotNull
    @Valid
    private final AuthorDto author;

    @JsonProperty("title")
    @NotNull
    @Size(min = 1, max = MAX_LENGTH_TITLE)
    private final String title;

    @JsonProperty("description")
    @NotNull
    @Size(min = 1, max = MAX_LENGHT_DESCRIPTION)
    private final String description;

    @JsonProperty("relatedItem")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedItem;

    @JsonProperty("relatedLot")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String relatedLot;

    @JsonCreator
    public CreateEnquiryRQDto(
        @JsonProperty("author") final AuthorDto author,
        @JsonProperty("title") final String title,
        @JsonProperty("description") final String description,
        @JsonProperty("relatedItem") final String relatedItem,
        @JsonProperty("relatedLot") final String relatedLot) {

        this.author = author;
        this.title = title;
        this.description = description;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
    }
}
