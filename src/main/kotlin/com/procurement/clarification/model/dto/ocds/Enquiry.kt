package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Enquiry @JsonCreator constructor(

        var id: String?,

        var date: LocalDateTime?,

        @field:Valid
        @field:NotNull
        val author: OrganizationReference,

        @field:NotNull
        val title: String,

        @field:NotNull
        val description: String,

        var dateAnswered: LocalDateTime?,

        val relatedItem: String?,

        val relatedLot: String?,

        var answer: String?
)