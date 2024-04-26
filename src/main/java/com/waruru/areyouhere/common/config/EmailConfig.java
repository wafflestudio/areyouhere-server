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
    private String username;
    private String password;
    private String host;
    private int port;

    @Profile({"develop", "release"})
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setPort(port);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.starttls.required", "true");


        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }

    @Profile("local")
    @Bean
    public JavaMailSender getLocalJavaMailSender() {
        return new JavaMailSenderImpl();
    }
}
