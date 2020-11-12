package com.procurement.clarification.infrastructure.bind.date

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.clarification.domain.extension.format
import java.time.LocalDateTime

class JsonDateSerializer : JsonSerializer<LocalDateTime>() {
    companion object {
        fun serialize(date: LocalDateTime): String = date.format()
    }

    override fun serialize(date: LocalDateTime, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(serialize(date))
}
