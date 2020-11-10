package com.procurement.clarification.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.clarification.infrastructure.bind.configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration(@Autowired objectMapper: ObjectMapper) {

    init {
        objectMapper.apply { configuration() }
    }
}
