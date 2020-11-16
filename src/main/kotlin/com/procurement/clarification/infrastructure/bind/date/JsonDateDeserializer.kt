package com.procurement.clarification.infrastructure.bind.date

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.procurement.clarification.domain.extension.parseLocalDateTime
import java.time.LocalDateTime

class JsonDateDeserializer : JsonDeserializer<LocalDateTime>() {
    companion object {
        fun deserialize(value: String): LocalDateTime = value.parseLocalDateTime()
    }

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocalDateTime =
        deserialize(jsonParser.text)
}
