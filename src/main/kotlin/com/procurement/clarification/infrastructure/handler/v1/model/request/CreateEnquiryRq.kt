package com.procurement.clarification.infrastructure.handler.v1.model.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.ocds.Address
import com.procurement.clarification.model.dto.ocds.ContactPoint
import com.procurement.clarification.model.dto.ocds.Details
import com.procurement.clarification.model.dto.ocds.Identifier
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class CreateEnquiryRq @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val enquiry: EnquiryCreate
)

data class EnquiryCreate @JsonCreator constructor(

    @field:Valid
        @field:NotNull
        val author: OrganizationReferenceCreate,

    @field:NotNull
        val title: String,

    @field:NotNull
        val description: String,

    val relatedItem: String?,

    val relatedLot: String?
)

data class OrganizationReferenceCreate @JsonCreator constructor(

        @field:NotNull
        val name: String,

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
        val contactPoint: ContactPoint,

        @field:Valid
        @field:NotNull
        var details: Details
)


