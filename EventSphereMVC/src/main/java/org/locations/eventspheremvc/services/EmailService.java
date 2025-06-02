package org.locations.eventspheremvc.services;

import DTOs.messageDTO;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.locations.eventspheremvc.Exceptions.EmailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private String link = "localhost:8081/reset?token=";
    private JavaMailSender javaMailSender;
    private final String mail = "eventSphere@wp.pl";

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordLink(String toEmail,String token){
        String subject = "Update account information";
        String htmlContent = "<p>Use this link to update account information:</p>" +
                "<a href=\"" + link + token + "\">Update Password</a> "
                + link + token;
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(mail);
            javaMailSender.send(message);
        }catch (MessagingException e){
            throw new EmailException("Email sending failure",new messageDTO(mail,subject,htmlContent));
        }
    }
    public void sendMessage(messageDTO message){
        try{
        MimeMessage email = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email,true,"UTF-8");
        helper.setSubject(message.getSubject());
        helper.setTo(message.getTo());
        helper.setText(message.getMessage());
        helper.setFrom(mail);
        javaMailSender.send(email);
        }catch (MessagingException e){
            throw new EmailException("Email sending failure",message);
        }
    }
    public void SendQrCode(String mailTo,byte[] qrCodeBytes){
        String text = "Check your QR code image in attachment";
        String subject = "Your ticcket is here :)";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(text);
            helper.setFrom(mail);
            DataSource dataSource = new ByteArrayDataSource(qrCodeBytes, "image/png"); // change MIME type if needed
            helper.addAttachment("QrCode", dataSource);
        } catch (MessagingException e) {
            throw new EmailException("Email sending failure",new messageDTO(mailTo,subject,"Mail with QR Code"));
        }
        javaMailSender.send(message);
    }
}
