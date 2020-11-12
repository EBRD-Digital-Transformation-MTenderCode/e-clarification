package com.procurement.clarification.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.infrastructure.bind.api.Action

interface Handler<T : Action, R: Any> {
    val action: T
    fun handle(node: JsonNode): R
}
