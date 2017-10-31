package com.procurement.clarification.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.procurement.clarification.model.entity")
@EnableJpaRepositories(basePackages = "com.procurement.clarification.repository")
@EnableTransactionManagement
public class DatabaseConfig {
}