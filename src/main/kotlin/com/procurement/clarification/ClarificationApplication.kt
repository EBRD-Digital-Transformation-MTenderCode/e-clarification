package com.procurement.clarification

import com.procurement.clarification.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
@EnableEurekaClient
class ClarificationApplication

fun main(args: Array<String>) {
    runApplication<ClarificationApplication>(*args)
}