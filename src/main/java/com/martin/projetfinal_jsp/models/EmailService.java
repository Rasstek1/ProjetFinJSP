package com.martin.projetfinal_jsp.models;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("votreadresse@gmail.com");
        mailSender.setPassword("votremotdepasse");

        javaMailSender = mailSender;

        // Configuration additionnelle si nécessaire
        // javaMailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");
        // javaMailSender.getJavaMailProperties().put("mail.smtp.auth", "true");
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
}


