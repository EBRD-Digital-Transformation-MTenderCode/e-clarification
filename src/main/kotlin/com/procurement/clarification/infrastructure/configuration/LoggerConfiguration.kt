package com.procurement.clarification.infrastructure.configuration

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.infrastructure.service.CustomLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoggerConfiguration {
    @Bean
    fun logger(): Logger = CustomLogger()
}
