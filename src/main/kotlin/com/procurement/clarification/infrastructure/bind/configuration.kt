package com.procurement.clarification.infrastructure.bind

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.procurement.clarification.infrastructure.bind.api.command.id.CommandIdModule
import com.procurement.clarification.infrastructure.bind.api.v2.incident.id.IncidentModule
import com.procurement.clarification.infrastructure.bind.api.version.ApiVersionModule
import com.procurement.clarification.infrastructure.bind.date.JsonDateModule
import java.math.BigDecimal

fun ObjectMapper.configuration() {
    val module = SimpleModule().apply {
        addDeserializer(String::class.java, StringsDeserializer())
        addDeserializer(Int::class.java, IntDeserializer())
        addDeserializer(BigDecimal::class.java, MoneyDeserializer())
    }

    registerModule(module)
    registerModule(ApiVersionModule())
    registerModule(CommandIdModule())
    registerModule(IncidentModule())
    registerModule(JsonDateModule())
    registerKotlinModule()

    configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
    configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
    configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)
    configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false)

    nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
}
