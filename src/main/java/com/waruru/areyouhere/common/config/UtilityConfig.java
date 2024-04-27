package com.waruru.areyouhere.common.config;

import com.waruru.areyouhere.common.utils.random.AlphanumericIdGenerator;
import com.waruru.areyouhere.common.utils.random.RandomIdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityConfig {
    @Bean
    public RandomIdentifierGenerator randomIdentifierGenerator() {
        return new AlphanumericIdGenerator();
    }
}
