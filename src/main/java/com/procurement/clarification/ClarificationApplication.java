package com.procurement.clarification;

import com.procurement.clarification.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackageClasses = {ApplicationConfig.class},
    exclude = {LiquibaseAutoConfiguration.class})
public class ClarificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClarificationApplication.class, args);
    }
}
