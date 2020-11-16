package com.procurement.clarification.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.clarification.application.service.Transform
import com.procurement.clarification.infrastructure.service.JacksonJsonTransform
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TransformConfiguration(private val objectMapper: ObjectMapper) {

    @Bean
    fun transform(): Transform = JacksonJsonTransform(mapper = objectMapper)
}
