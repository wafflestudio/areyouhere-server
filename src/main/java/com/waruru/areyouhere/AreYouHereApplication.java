package com.waruru.areyouhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AreYouHereApplication {

	public static void main(String[] args) {
		SpringApplication.run(AreYouHereApplication.class, args);
	}

}
