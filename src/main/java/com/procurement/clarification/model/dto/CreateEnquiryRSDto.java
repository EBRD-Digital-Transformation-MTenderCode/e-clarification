package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import com.procurement.clarification.model.dto.AuthorDto;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
    "signOfResponse",
    "relatedItem",
    "relatedLot"
})
public class CreateEnquiryRSDto {
    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @Valid
    @JsonProperty("date")
    private LocalDateTime date;
    @NotNull
    @JsonProperty("author")
    private final AuthorDto author;
    @NotNull
    @JsonProperty("title")
    private final String title;
    @NotNull
    @JsonProperty("description")
    private final String description;
    @NotNull
    @JsonProperty("signOfResponse")
    private final Boolean signOfResponse;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("relatedItem")
    private final String relatedItem;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("relatedLot")
    private final String relatedLot;

    @JsonCreator
    public CreateEnquiryRSDto(
        @NotNull
        @JsonProperty("id") final String id,
                              @JsonSerialize(using = LocalDateTimeSerializer.class)
                              @NotNull
                              @JsonProperty("date") final LocalDateTime date,
                              @NotNull
                              @Valid
                              @JsonProperty("author") final AuthorDto author,
                              @NotNull
                              @JsonProperty("title") final String title,
                              @NotNull
                              @JsonProperty("description") final String description,
                              @NotNull
                              @JsonProperty("signOfResponse") final Boolean signOfResponse,
                              @JsonInclude(JsonInclude.Include.NON_NULL)
                              @JsonProperty("relatedItem") final String relatedItem,
                              @JsonInclude(JsonInclude.Include.NON_NULL)
                              @JsonProperty("relatedLot") final String relatedLot) {

        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.signOfResponse = signOfResponse;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
    }
}
