package com.martin.projetfinal_jsp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // Création d'une instance de JavaMailSenderImpl pour envoyer des e-mails
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configuration du serveur SMTP de Gmail
        mailSender.setHost("smtp.gmail.com"); // Définition de l'hôte SMTP (Gmail)
        mailSender.setPort(587); // Définition du port SMTP (587 est le port TLS de Gmail)

        // Configuration des informations d'identification (nom d'utilisateur et mot de passe) pour se connecter à Gmail
        mailSender.setUsername("nitramenicar@gmail.com"); // Remplacez par votre propre adresse e-mail Gmail
        mailSender.setPassword("IsaLeoEliot1"); // Remplacez par votre propre mot de passe Gmail

        // Configuration des propriétés pour activer le chiffrement TLS et l'authentification SMTP
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true"); // Activation du chiffrement TLS
        props.put("mail.smtp.auth", "true"); // Activation de l'authentification SMTP

        // Retourne l'instance configurée de JavaMailSenderImpl pour être utilisée par l'application
        return mailSender;
    }
}

