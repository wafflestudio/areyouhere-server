package com.waruru.areyouhere.common.config;

import java.util.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfig {
    private String email;
    private String password;
    private String host;
    private int port;

    @Profile({"develop", "release"})
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        mailSender.setPort(port);
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }

    @Profile("local")
    @Bean
    public JavaMailSender getLocalJavaMailSender() {
        return new JavaMailSenderImpl();
    }
}
