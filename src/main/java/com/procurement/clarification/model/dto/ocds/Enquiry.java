package com.procurement.clarification.model.dto.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.LocalDateTimeDeserializer;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
public class Enquiry {

    @JsonProperty("id")
    private String id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("date")
    private LocalDateTime date;

    @Valid
    @NotNull
    @JsonProperty("author")
    private final OrganizationReference author;

    @NotNull
    @JsonProperty("title")
    private final String title;

    @NotNull
    @JsonProperty("description")
    private final String description;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("dateAnswered")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateAnswered;

    @JsonProperty("relatedItem")
    private final String relatedItem;

    @JsonProperty("relatedLot")
    private final String relatedLot;

    @JsonCreator
    public Enquiry(
            @JsonProperty("id") final String id,
            @JsonProperty("date") final LocalDateTime date,
            @JsonProperty("author") final OrganizationReference author,
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(date)
                .append(author)
                .append(title)
                .append(description)
                .append(answer)
                .append(dateAnswered)
                .append(relatedItem)
                .append(relatedLot)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Enquiry)) {
            return false;
        }
        final Enquiry rhs = (Enquiry) other;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(date, rhs.date)
                .append(author, rhs.author)
                .append(title, rhs.title)
                .append(description, rhs.description)
                .append(answer, rhs.answer)
                .append(dateAnswered, rhs.dateAnswered)
                .append(relatedItem, rhs.relatedItem)
                .append(relatedLot, rhs.relatedLot)
                .isEquals();
    }
}
