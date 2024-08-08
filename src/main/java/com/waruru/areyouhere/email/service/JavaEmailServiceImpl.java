package com.waruru.areyouhere.email.service;

import com.waruru.areyouhere.email.domain.MessageHolder;
import com.waruru.areyouhere.email.domain.MessageTemplate;
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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "spring.mail")
@Slf4j
public class JavaEmailServiceImpl implements EmailService {

    private String from;
    private String username;
    private String password;
    private String host;
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
        MimeMessage msg = new MimeMessage(session);

        try {
            setMessage(to, title, content, msg);
            doSend(session, msg);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


    }

    private void doSend(Session session, MimeMessage msg) throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect(host, username, password);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }

    private void setMessage(String to, String title, String content, MimeMessage msg)
            throws MessagingException, UnsupportedEncodingException {
        msg.setFrom(new InternetAddress(from, from));
        InternetAddress internetAddress = new InternetAddress(to);
        internetAddress.validate();
        msg.setRecipient(RecipientType.TO, internetAddress);
        msg.setSubject(title);
        msg.setContent(content, "text/html");
        msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
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
