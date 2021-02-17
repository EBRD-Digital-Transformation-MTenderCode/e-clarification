package com.procurement.clarification.infrastructure.handler.v1.model.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.clarification.model.dto.ocds.Address
import com.procurement.clarification.model.dto.ocds.AddressDetails
import com.procurement.clarification.model.dto.ocds.ContactPoint
import com.procurement.clarification.model.dto.ocds.CountryDetails
import com.procurement.clarification.model.dto.ocds.Details
import com.procurement.clarification.model.dto.ocds.Identifier
import com.procurement.clarification.model.dto.ocds.LocalityDetails
import com.procurement.clarification.model.dto.ocds.RegionDetails
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
    val additionalIdentifiers: List<Identifier>?,

    @field:Valid
    @field:NotNull
    val contactPoint: ContactPoint,

    @field:Valid
    @field:NotNull
    var details: Details
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Address @JsonCreator constructor(

        @field:NotNull
        val streetAddress: String,

        val postalCode: String?,

        @field:Valid @field:NotNull
        val addressDetails: AddressDetails
    ) {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        data class AddressDetails(

            @field:Valid @field:NotNull
            val country: CountryDetails,

            @field:Valid @field:NotNull
            val region: RegionDetails,

            @field:Valid @field:NotNull
            val locality: LocalityDetails
        ) {
            @JsonInclude(JsonInclude.Include.NON_NULL)
            data class CountryDetails(

                var scheme: String,

                @field:NotNull
                val id: String,

                var description: String,

                var uri: String
            )

            @JsonInclude(JsonInclude.Include.NON_NULL)
            data class RegionDetails(

                var scheme: String,

                @field:NotNull
                val id: String,

                var description: String,

                var uri: String
            )

            @JsonInclude(JsonInclude.Include.NON_NULL)
            data class LocalityDetails(

                @field:NotNull
                var scheme: String,

                @field:NotNull
                val id: String,

                @field:NotNull
                var description: String,

                var uri: String?
            )
        }
    }
}


