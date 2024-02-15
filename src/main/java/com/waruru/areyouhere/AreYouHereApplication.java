package com.waruru.areyouhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AreYouHereApplication {

	public static void main(String[] args) {
		SpringApplication.run(AreYouHereApplication.class, args);
	}

}
