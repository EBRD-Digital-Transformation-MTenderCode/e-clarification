package com.procuremen.clarification;

import com.procuremen.clarification.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackageClasses = {ApplicationConfig.class},
    exclude = {LiquibaseAutoConfiguration.class})
@EnableSwagger2
public class ClarificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClarificationApplication.class, args);
    }
}
