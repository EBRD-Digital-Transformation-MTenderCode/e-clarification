package com.procurement.clarification.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@ComponentScan(basePackages = "com.procurement.clarification.model.entity")
@EnableCassandraRepositories(basePackages = "com.procurement.clarification.repository")
public class DatabaseConfig {
}
