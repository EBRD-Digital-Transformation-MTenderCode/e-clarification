package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class OrganizationReference @JsonCreator constructor(

        @field:NotNull
        val name: String,

        var id: String?,

        @field:Valid
        @field:NotNull
        val identifier: Identifier,

        @field:Valid
        @field:NotNull
        val address: Address,

        @field:Valid
        val additionalIdentifiers: Set<Identifier>?,

        @field:Valid
        @field:NotNull
        val contactPoint: ContactPoint
)
