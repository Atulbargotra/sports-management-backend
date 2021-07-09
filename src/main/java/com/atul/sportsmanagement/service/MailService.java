package com.atul.sportsmanagement.service;


import com.atul.sportsmanagement.exceptions.SpringSportsException;
import com.atul.sportsmanagement.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
@Slf4j
class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    private TemplateEngine templateEngine;

    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("sports@tiaa.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringSportsException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }
    }
    @Async
    public void prepareAndSend(String recipient, String name, String team, String event, String url) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("sports@tiaa.com");
            messageHelper.setTo(recipient);
            messageHelper.setSubject("Team Invite");
            String content = mailContentBuilder.buildHtml(name,team,event,url);
            messageHelper.setText(content, true);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime exception; compiler will not force you to handle it
        }
    }




}