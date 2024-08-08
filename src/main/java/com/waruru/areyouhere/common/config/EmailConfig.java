package com.waruru.areyouhere.common.config;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


//@Configuration
//@ConfigurationProperties(prefix = "spring.mail")
//@Slf4j
//public class EmailConfig {
//    private String username;
//    private String password;
//    private String host;
//    private int port;
//
//    @Profile({"develop", "release"})
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        log.info(username);
//        mailSender.setHost(host);
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//        mailSender.setPort(port);
//
//        Properties javaMailProperties = getProperties();
//
//        mailSender.setJavaMailProperties(javaMailProperties);
//
//        return mailSender;
//    }
//
//    private Properties getProperties() {
//        Properties javaMailProperties = new Properties();
//        javaMailProperties.put("mail.transport.protocol", "smtp");
//        javaMailProperties.put("mail.smtp.port", port);
//        javaMailProperties.put("mail.smtp.ssl.enable", "true");
//        javaMailProperties.put("mail.smtp.auth", "true");
//        javaMailProperties.put("mail.smtp.auth.login.disable", "true");
//        javaMailProperties.put("mail.smtp.starttls.enable", "true");
//        javaMailProperties.put("mail.smtp.starttls.required", "true");
//        return javaMailProperties;
//    }
//
//    @Profile("local")
//    @Bean
//    public JavaMailSender getLocalJavaMailSender() {
//        return new JavaMailSenderImpl();
//    }
//}
