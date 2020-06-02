package com.procurement.clarification.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.procurement.clarification.service",
        "com.procurement.clarification.infrastructure.handler",
        "com.procurement.clarification.application.service"
    ]
)
class ServiceConfig
