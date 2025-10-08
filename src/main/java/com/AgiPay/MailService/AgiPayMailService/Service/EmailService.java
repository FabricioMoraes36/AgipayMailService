package com.AgiPay.MailService.AgiPayMailService.Service;

import com.AgiPay.MailService.AgiPayMailService.Enums.EmailSubject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendVerificationEmail(String to, String subject, String htmlContent) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true para HTML
            mailSender.send(message);
            logger.info("Email enviado para {}", to);
        } catch (MessagingException e) {
            logger.error("Falha ao enviar email para {}: {}", to, e.getMessage());
            throw e;
        }
    }

    public void sendWelcomeEmail(String to, String name) throws MessagingException {
        String htmlContent = """
        <html>
        <head>
            <style>
                h1 { color: #4CAF50; }
                p { font-size: 16px; }
            </style>
        </head>
        <body>
            <h1>Bem-vindo, %s!</h1>
            <p>Obrigado por se cadastrar na AgiPay.</p>
        </body>
        </html>
        """.formatted(name);
        sendVerificationEmail(to, EmailSubject.WELCOME.getSubject(), htmlContent);
    }

    public void sendPaymentReceivedEmail(String to, String name) throws MessagingException {
        String htmlContent = """
        <html>
        <head>
            <style>
                h1 { color: #2196F3; }
                p { font-size: 16px; }
            </style>
        </head>
        <body>
            <h1>Olá, %s!</h1>
            <p>Seu pagamento foi recebido com sucesso.</p>
        </body>
        </html>
        """.formatted(name);
        sendVerificationEmail(to, EmailSubject.PAYMENT_RECEIVED.getSubject(), htmlContent);
    }
}
