package org.locations.eventspheremvc.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private String link = "localhost:8081/reset?token=";
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordLink(String toEmail,String token){
        String subject = "Update account information";
        String htmlContent = "<p>Use this link to update account information:</p>"+
                "<a href=\"" + link + token + "\">Update Password</a>";
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent,true);
            helper.setFrom("eventSphere@wp.pl");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Something went wrong during mail sending",e);
        }
    }
}
