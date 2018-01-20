package com.procurement.clarification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ClarificationApplication {
    public static void main(final String[] args) {
        SpringApplication.run(ClarificationApplication.class, args);
    }
}
