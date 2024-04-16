package com.interswitchgroup.accountmanagementsystem.common.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String emailTo, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setFrom("okorojeremiah91@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
}
