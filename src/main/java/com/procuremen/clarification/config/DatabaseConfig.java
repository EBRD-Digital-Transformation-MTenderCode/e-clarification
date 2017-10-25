package com.procuremen.clarification.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.procurement.mdm.model.entity")
@EnableJpaRepositories(basePackages = "com.procurement.mdm.repository")
@EnableTransactionManagement
public class DatabaseConfig {
}