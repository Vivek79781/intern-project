package com.project.pagerduty.services;

import com.project.pagerduty.models.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void sendMail(String to, String engineerId, Alert alert) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Alert Title: "+alert.getTitle());
        helper.setText("Alert ID: "+alert.getId()+"<ul><li>Alert Title: "+alert.getTitle()+"</li>\n<li>Alert Description: "+alert.getDescription()+"</li>\n<a href=\"http://localhost:9090/engineer/"+engineerId+"/alert/"+alert.getId()+"\">Click Here To Acknowledge</a>", true);
        mailSender.send(message);
    }
}
