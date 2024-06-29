package com.waruru.areyouhere.email.service;

import com.waruru.areyouhere.email.domain.MessageHolder;
import com.waruru.areyouhere.email.domain.MessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class JavaEmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;


    public void sendVerifyEmail(String to, String title, String verificationLink, MessageTemplate messageTemplate) {
        MessageHolder messageHolder = new MessageHolder(title, messageTemplate, verificationLink);
        sendSimpleMessage(to, messageHolder.getTitle(), messageHolder.getContents());
    }

    private void sendSimpleMessage(String to, String title, String content) {
        if (from == null || from.isEmpty()) {
            log.info("Email text Test: " + content);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        emailSender.send(message);
    }

}
