package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.NotNull

data class Address @JsonCreator constructor(

        @field:NotNull
        val streetAddress: String,

        @field:NotNull
        val locality: String,

        @field:NotNull
        val region: String,

        val postalCode: String?,

        @field:NotNull
        val countryName: String
)