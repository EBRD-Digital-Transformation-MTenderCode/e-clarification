package com.procurement.clarification.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.procurement.clarification.infrastructure.bind.ApiVersionDeserializer
import com.procurement.clarification.infrastructure.bind.ApiVersionSerializer
import com.procurement.clarification.infrastructure.dto.ApiVersion
import com.procurement.clarification.model.dto.databinding.IntDeserializer
import com.procurement.clarification.model.dto.databinding.JsonDateDeserializer
import com.procurement.clarification.model.dto.databinding.JsonDateSerializer
import com.procurement.clarification.model.dto.databinding.MoneyDeserializer
import com.procurement.clarification.model.dto.databinding.StringsDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.time.LocalDateTime

@Configuration
class ObjectMapperConfig(@Autowired objectMapper: ObjectMapper) {

    init {
        val module = SimpleModule()
        module.addSerializer(LocalDateTime::class.java, JsonDateSerializer())
        module.addDeserializer(LocalDateTime::class.java, JsonDateDeserializer())
        module.addDeserializer(BigDecimal::class.java, MoneyDeserializer())
        module.addDeserializer(String::class.java, StringsDeserializer())
        module.addDeserializer(Int::class.java, IntDeserializer())

        /*
         * Serializer/Deserializer for ApiVersion type
         */
        module.addSerializer(ApiVersion::class.java, ApiVersionSerializer())
        module.addDeserializer(ApiVersion::class.java, ApiVersionDeserializer())

        objectMapper.registerModule(module)
        objectMapper.registerKotlinModule()
        objectMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
    }
}

