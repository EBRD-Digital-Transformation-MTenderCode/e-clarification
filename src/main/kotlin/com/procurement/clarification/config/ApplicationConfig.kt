package com.procurement.clarification.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(DaoConfiguration::class, ServiceConfig::class, WebConfig::class, ObjectMapperConfig::class)
class ApplicationConfig
