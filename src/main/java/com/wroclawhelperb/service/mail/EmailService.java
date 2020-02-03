package com.wroclawhelperb.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    @Autowired
    private JavaMailSender javaMailSender;

    private static EmailService emailServiceInstance = null;

    public static EmailService getInstance() {
        if (emailServiceInstance == null) {
            emailServiceInstance = new EmailService();
        }
        return emailServiceInstance;
    }

    private EmailService() {
    }

    private MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getSendTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mail.getMessage());
        };
    }

    void send(final Mail mail) {
        LOGGER.info("Starting email preparation...");
        try {
            javaMailSender.send(createMimeMessage(mail));
            LOGGER.info("Email has been sent.");
        } catch(MailException e) {
            LOGGER.error("Failed to process email sending: " + e.getMessage(), e);
        }
    }
}
