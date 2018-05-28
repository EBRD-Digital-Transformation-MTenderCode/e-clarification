package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReference(

        @JsonProperty("name") @NotNull
        val name: String,

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("identifier") @Valid @NotNull
        val identifier: Identifier,

        @JsonProperty("address") @Valid @NotNull
        val address: Address,

        @JsonProperty("additionalIdentifiers") @Valid
        val additionalIdentifiers: Set<Identifier>?,

        @JsonProperty("contactPoint") @Valid @NotNull
        val contactPoint: ContactPoint
)
