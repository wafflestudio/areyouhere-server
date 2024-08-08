package com.waruru.areyouhere.email.service;

import com.waruru.areyouhere.email.domain.MessageHolder;
import com.waruru.areyouhere.email.domain.MessageTemplate;
import com.waruru.areyouhere.email.exception.EmailSendException;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class JavaEmailServiceImpl implements EmailService {

    @Value("${spring.mail.from}")
    private String from;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;



    public void sendVerifyEmail(String to, String title, String verificationLink, MessageTemplate messageTemplate) {
        MessageHolder messageHolder = new MessageHolder(title, messageTemplate, verificationLink);
        sendSimpleMessage(to, messageHolder.getTitle(), messageHolder.getContents());
    }

    private void sendSimpleMessage(String to, String title, String content)  {
        if (from == null || from.isEmpty()) {
            log.info("Email text Test: " + content);
            return;
        }

        Session session = getSession();
        try {
            MimeMessage msg = setMessage(to, from, content, session);
            doSend(session, msg);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailSendException();
        }


    }

    private void doSend(Session session, MimeMessage msg) throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect(host, username, password);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }

    private MimeMessage setMessage(String to, String title, String content, Session session)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, from));
        InternetAddress internetAddress = new InternetAddress(to);
        internetAddress.validate();
        msg.setRecipient(RecipientType.TO, internetAddress);
        msg.setSubject(title);
        msg.setContent(content, "text/html");
        msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
        return msg;
    }

    private Session getSession(){
        Properties emailProps = System.getProperties();
        emailProps.put("mail.transport.protocol", "smtp");
        emailProps.put("mail.smtp.port", port);
        emailProps.put("mail.smtp.starttls.required", "true");
        emailProps.put("mail.smtp.auth.login.disable", "true");
        emailProps.put("mail.smtp.starttls.enable", "true");
        emailProps.put("mail.smtp.auth", "true");

        return Session.getDefaultInstance(emailProps);

    }

}
