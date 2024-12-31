package com.waruru.areyouhere.common.config;

import com.waruru.areyouhere.common.utils.random.AlphanumericIdGenerator;
import com.waruru.areyouhere.common.utils.random.RandomIdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UtilityConfig {
    @Bean
    public RandomIdentifierGenerator randomIdentifierGenerator() {
        return new AlphanumericIdGenerator();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
