package com.procurement.clarification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.clarification.utils.DateUtil;
import com.procurement.clarification.utils.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DatabaseConfig.class,
        ServiceConfig.class,
        WebConfig.class
})
public class ApplicationConfig {
    @Bean
    public JsonUtil jsonUtil(final ObjectMapper mapper) {
        return new JsonUtil(mapper);
    }

    @Bean
    public DateUtil dateUtil() {
        return new DateUtil();
    }
}
