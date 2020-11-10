package com.procurement.clarification.infrastructure.handler.v1.model.response

import com.fasterxml.jackson.annotation.JsonProperty


data class CheckAnswerRs(

        @get:JsonProperty("setUnsuspended")
        val setUnsuspended: Boolean
)
