package com.AppVendasBares.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarSenhaTemporaria(String email, String nome, String senhaTemporaria) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Recuperação de Senha - AppVendasBares");
            message.setText(String.format(
                    "Olá %s,\n\n" +
                    "Sua senha temporária é: %s\n\n" +
                    "Por favor, altere sua senha após o próximo login.\n\n" +
                    "Atenciosamente,\nEquipe AppVendasBares",
                    nome, senhaTemporaria
            ));
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
