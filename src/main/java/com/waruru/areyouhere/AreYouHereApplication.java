package com.waruru.areyouhere;

import com.waruru.areyouhere.common.annotation.SlackNotification;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AreYouHereApplication {

	public static void main(String[] args) {
		SpringApplication.run(AreYouHereApplication.class, args);
	}

	@PostConstruct
	void started(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}



}
