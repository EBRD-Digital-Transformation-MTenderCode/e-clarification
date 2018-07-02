package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.NotNull

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