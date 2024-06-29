package com.waruru.areyouhere.email.service;


import com.waruru.areyouhere.email.domain.MessageTemplate;

public interface EmailService {
    public void sendVerifyEmail(String to, String title, String verificationLink, MessageTemplate messageTemplate);
}
