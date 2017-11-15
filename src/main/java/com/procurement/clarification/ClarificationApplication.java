package com.procurement.clarification;

import com.procurement.clarification.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

@SpringBootApplication(scanBasePackageClasses = {ApplicationConfig.class},
    exclude = {LiquibaseAutoConfiguration.class})
public class ClarificationApplication {
    public static void main(final String[] args) {
        SpringApplication.run(ClarificationApplication.class, args);
    }
}
