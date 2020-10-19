package com.procurement.clarification.infrastructure.handler.enquiry.get

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.domain.model.country.CountryId
import com.procurement.clarification.domain.model.enquiry.EnquiryId
import com.procurement.clarification.domain.model.enums.Scale
import com.procurement.clarification.domain.model.lot.LotId
import java.time.LocalDateTime

data class GetEnquiryByIdsResult(
    @param:JsonProperty("id") @field:JsonProperty("id")
    val id: EnquiryId,
    @param:JsonProperty("date") @field:JsonProperty("date")
    val date: LocalDateTime,
    @param:JsonProperty("author") @field:JsonProperty("author")
    val author: Author,
    @param:JsonProperty("title") @field:JsonProperty("title")
    val title: String,
    @param:JsonProperty("description") @field:JsonProperty("description")
    val description: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("relatedLot") @field:JsonProperty("relatedLot")
    val relatedLot: LotId?,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("answer") @field:JsonProperty("answer")
    val answer: String?,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("dateAnswer") @field:JsonProperty("dateAnswer")
    val dateAnswer: LocalDateTime?
) {
    data class Author(
        @param:JsonProperty("name") @field:JsonProperty("name")
        val name: String,
        @param:JsonProperty("id") @field:JsonProperty("id")
        val id: String,
        @param:JsonProperty("identifier") @field:JsonProperty("identifier")
        val identifier: Identifier,
        @param:JsonProperty("address") @field:JsonProperty("address")
        val address: Address,
        @param:JsonProperty("contactPoint") @field:JsonProperty("contactPoint")
        val contactPoint: ContactPoint,
        @param:JsonProperty("details") @field:JsonProperty("details")
        val details: Details,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @param:JsonProperty("additionalIdentifiers") @field:JsonProperty("additionalIdentifiers")
        val additionalIdentifiers: List<AdditionalIdentifier>?
    ) {
        data class Identifier(
            @param:JsonProperty("id") @field:JsonProperty("id")
            val id: String,
            @param:JsonProperty("legalName") @field:JsonProperty("legalName")
            val legalName: String,
            @param:JsonProperty("scheme") @field:JsonProperty("scheme")
            val scheme: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("uri") @field:JsonProperty("uri")
            val uri: String?
        )

        data class Address(
            @param:JsonProperty("streetAddress") @field:JsonProperty("streetAddress")
            val streetAddress: String,
            @param:JsonProperty("addressDetails") @field:JsonProperty("addressDetails")
            val addressDetails: AddressDetails,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("postalCode") @field:JsonProperty("postalCode")
            val postalCode: String?
        ) {
            data class AddressDetails(
                @param:JsonProperty("country") @field:JsonProperty("country")
                val country: Country,
                @param:JsonProperty("region") @field:JsonProperty("region")
                val region: Region,
                @param:JsonProperty("locality") @field:JsonProperty("locality")
                val locality: Locality
            ) {
                data class Country(
                    @param:JsonProperty("id") @field:JsonProperty("id")
                    val id: CountryId,
                    @param:JsonProperty("description") @field:JsonProperty("description")
                    val description: String,
                    @param:JsonProperty("scheme") @field:JsonProperty("scheme")
                    val scheme: String,
                    @param:JsonProperty("uri") @field:JsonProperty("uri")
                    val uri: String
                )

                data class Region(
                    @param:JsonProperty("id") @field:JsonProperty("id")
                    val id: String,
                    @param:JsonProperty("description") @field:JsonProperty("description")
                    val description: String,
                    @param:JsonProperty("scheme") @field:JsonProperty("scheme")
                    val scheme: String,
                    @param:JsonProperty("uri") @field:JsonProperty("uri")
                    val uri: String
                )

                data class Locality(
                    @param:JsonProperty("id") @field:JsonProperty("id")
                    val id: String,
                    @param:JsonProperty("description") @field:JsonProperty("description")
                    val description: String,
                    @param:JsonProperty("scheme") @field:JsonProperty("scheme")
                    val scheme: String,
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @param:JsonProperty("uri") @field:JsonProperty("uri")
                    val uri: String?
                )
            }
        }

        data class AdditionalIdentifier(
            @param:JsonProperty("id") @field:JsonProperty("id")
            val id: String,
            @param:JsonProperty("legalName") @field:JsonProperty("legalName")
            val legalName: String,
            @param:JsonProperty("scheme") @field:JsonProperty("scheme")
            val scheme: String,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("uri") @field:JsonProperty("uri")
            val uri: String?
        )

        data class ContactPoint(
            @param:JsonProperty("name") @field:JsonProperty("name")
            val name: String,
            @param:JsonProperty("email") @field:JsonProperty("email")
            val email: String,
            @param:JsonProperty("telephone") @field:JsonProperty("telephone")
            val telephone: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("faxNumber") @field:JsonProperty("faxNumber")
            val faxNumber: String?,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("url") @field:JsonProperty("url")
            val url: String?
        )

        data class Details(
            @param:JsonProperty("scale") @field:JsonProperty("scale")
            val scale: Scale
        )
    }
}
