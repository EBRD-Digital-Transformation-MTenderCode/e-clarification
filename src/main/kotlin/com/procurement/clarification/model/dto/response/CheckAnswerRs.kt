package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonProperty


data class CheckAnswerRs(

        @get:JsonProperty("setUnsuspended")
        val setUnsuspended: Boolean
)
