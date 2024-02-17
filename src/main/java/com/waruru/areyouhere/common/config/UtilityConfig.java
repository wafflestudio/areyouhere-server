package com.waruru.areyouhere.common.config;

import com.waruru.areyouhere.common.utils.AlphanumericIdGenerator;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityConfig {
    @Bean
    public RandomIdentifierGenerator randomIdentifierGenerator() {
        return new AlphanumericIdGenerator();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){return new PasswordEncoder();}
}
