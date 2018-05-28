package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.clarification.model.dto.databinding.JsonDateDeserializer
import com.procurement.clarification.model.dto.databinding.JsonDateSerializer
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Enquiry(

        @JsonProperty("id")
        var id: String?,

        @JsonProperty("dateTime")
        @JsonSerialize(using = JsonDateSerializer::class)
        @JsonDeserialize(using = JsonDateDeserializer::class)
        var date: LocalDateTime?,

        @JsonProperty("author") @Valid @NotNull
        val author: OrganizationReference,

        @JsonProperty("title") @NotNull
        val title: String,

        @JsonProperty("description") @NotNull
        val description: String,

        @JsonProperty("dateAnswered")
        @JsonSerialize(using = JsonDateSerializer::class)
        var dateAnswered: LocalDateTime?,

        @JsonProperty("relatedItem")
        val relatedItem: String?,

        @JsonProperty("relatedLot")
        val relatedLot: String?,

        @JsonProperty("answer")
        var answer: String?
)