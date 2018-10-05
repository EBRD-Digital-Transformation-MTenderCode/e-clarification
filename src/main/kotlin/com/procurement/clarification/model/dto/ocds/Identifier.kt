package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Identifier @JsonCreator constructor(

        val scheme: String,

        val id: String,

        val legalName: String,

        val uri: String?
)