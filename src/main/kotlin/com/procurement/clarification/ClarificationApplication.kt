package com.procurement.clarification

import com.procurement.clarification.infrastructure.configuration.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
class ClarificationApplication

fun main(args: Array<String>) {
    runApplication<ClarificationApplication>(*args)
}