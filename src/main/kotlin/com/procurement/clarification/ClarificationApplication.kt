package com.procurement.clarification

import com.procurement.clarification.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
class ClarificationApplication

fun main(args: Array<String>) {
    runApplication<ClarificationApplication>(*args)
}