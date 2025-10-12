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
        String imageUrl = "https://i.imgur.com/ti2kwEW.png";
        String htmlContent = """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bem-vindo(a) à AgiPay!</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            color: #333;
        }
        .email-container {
            max-width: 600px;
            margin: 20px auto;
            background-color: #ffffff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .header {
            background-color: #007bff;
            color: #ffffff;
            text-align: center;
            padding: 20px;
        }
        .header h1 {
            margin: 0;
            font-size: 24px;
        }
        .content {
            padding: 20px 30px;
            line-height: 1.6;
        }
        .content h2 {
            color: #007bff;
            margin-top: 0;
        }
        .content ul {
            list-style-type: none;
            padding: 0;
        }
        .content ul li {
            margin-bottom: 10px;
            position: relative;
            padding-left: 25px;
        }
        .content ul li:before {
            content: "✓";
            color: #28a745;
            font-weight: bold;
            position: absolute;
            left: 0;
        }
        .button-container {
            text-align: center;
            margin: 30px 0;
        }
        .button {
            display: inline-block;
            background-color: #007bff;
            color: #ffffff;
            text-decoration: none;
            padding: 12px 25px;
            border-radius: 5px;
            font-weight: bold;
        }
        .footer {
            text-align: center;
            padding: 20px;
            font-size: 12px;
            color: #777;
            border-top: 1px solid #eee;
        }
        .banner {
            width: 100%%;
            height: auto;
            display: block;
            margin: 0;
        }
        .banner img {
            width: 100%%;
            height: auto;
            display: block;
        }
    </style>
</head>
<body>
    <div class="email-container">
        <div class="header">
            <h1>Bem-vindo(a) à AgiPay!</h1>
        </div>
        <div class="content">
            <p>Olá, %s!</p>
            <p>Seja muito bem-vindo(a) à AgiPay, a sua mais nova plataforma de pagamento digital! Estamos felizes em ter você conosco e prontos para simplificar a sua vida financeira.</p>
            <p>Com a AgiPay, você pode:</p>
            <ul>
                <li><strong>Pagar contas e boletos</strong> de forma rápida e segura.</li>
                <li><strong>Realizar transferências</strong> instantâneas para amigos e familiares.</li>
                <li><strong>Gerenciar suas finanças</strong> de um jeito simples e intuitivo.</li>
            </ul>
            <p>Para começar, que tal explorar a sua conta? Clique no botão abaixo para acessar a sua plataforma.</p>
            <div class="button-container">
                <a href="URL_DA_SUA_PLATAFORMA" class="button">Acessar Minha Conta</a>
            </div>
            <p>Se precisar de ajuda ou tiver alguma dúvida, não hesite em entrar em contato com nossa equipe de suporte.</p>
            <p>Até breve!</p>
            <p>Equipe AgiPay</p>
        </div>
        <div class="banner">
            <img src="%s" alt="Pessoas usando a plataforma AgiPay">
        </div>
        <div class="footer">
            <p>&copy; 2025 AgiPay. Todos os direitos reservados.</p>
        </div>
    </div>
</body>
</html>
""".formatted(name, imageUrl);

        sendVerificationEmail(to, EmailSubject.WELCOME.getSubject(), htmlContent);
    }

    public void sendPaymentReceivedEmail(String to, String name) throws MessagingException {
        String htmlContent = """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Pagamento Recebido</title>
<style>
    body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
        background-color: #f7f7f7;
        margin: 0;
        padding: 20px;
        color: #222;
    }
    .container {
        max-width: 600px;
        margin: 0 auto;
        background: #ffffff;
        border-radius: 10px;
        padding: 26px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    }
    h1 {
        color: #0b5ed7;
        font-size: 30px;
        margin: 0 0 12px;
        line-height: 1.1;
    }
    p {
        font-size: 20px;
        line-height: 1.6;
        margin: 10px 0;
    }
    .summary {
        background: #f0f8ff;
        border-left: 5px solid #0b5ed7;
        padding: 14px;
        border-radius: 6px;
        margin-top: 14px;
        font-size: 18px;
    }
    .button {
        display: inline-block;
        background: #0b5ed7;
        color: #ffffff;
        text-decoration: none;
        padding: 14px 22px;
        border-radius: 8px;
        font-size: 18px;
        font-weight: 600;
        margin-top: 18px;
    }
    .footer {
        font-size: 15px;
        color: #555;
        margin-top: 20px;
    }
    @media (max-width: 480px) {
        h1 { font-size: 26px; }
        p { font-size: 18px; }
        .button { font-size: 16px; padding: 12px 18px; }
    }
</style>
</head>
<body>
  <div class="container">
    <h1>Olá, %s</h1>
    <p>Seu pagamento foi recebido com sucesso. Obrigado por usar a AgiPay.</p>

    <div class="summary" role="note" aria-label="Resumo do pagamento">
      <p><strong>Status:</strong> Pago</p>
      <p><strong>Valor:</strong> —</p>
      <p><strong>Data:</strong> —</p>
    </div>

    <a href="#" class="button" role="button" aria-label="Ver detalhes da transação">Ver detalhes da transação</a>

    <p class="footer">Se precisar de ajuda, responda este e‑mail ou acesse nosso suporte.</p>
  </div>
</body>
</html>
""".formatted(name);

        sendVerificationEmail(to, EmailSubject.PAYMENT_RECEIVED.getSubject(), htmlContent);
    }
    public void sendPaymentSuccessEmail(String to, String name) throws MessagingException {
        String htmlContent = """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Pagamento Confirmado</title>
<style>
    body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
        background-color: #f6f9fc;
        margin: 0;
        padding: 24px;
        color: #111;
    }
    .container {
        max-width: 650px;
        margin: 0 auto;
        background: #ffffff;
        border-radius: 12px;
        padding: 28px;
        box-shadow: 0 6px 20px rgba(17,24,39,0.06);
    }
    h1 {
        color: #0a64c8;
        font-size: 34px;
        margin: 0 0 14px;
        line-height: 1.05;
    }
    p {
        font-size: 20px;
        line-height: 1.6;
        margin: 10px 0;
    }
    .highlight {
        background: #fff8e1;
        border-left: 6px solid #f6b73c;
        padding: 14px;
        border-radius: 8px;
        font-size: 18px;
        margin-top: 14px;
    }
    .details {
        margin-top: 16px;
        font-size: 19px;
    }
    .button {
        display: inline-block;
        background: #0a64c8;
        color: #ffffff;
        text-decoration: none;
        padding: 14px 24px;
        border-radius: 10px;
        font-size: 18px;
        font-weight: 700;
        margin-top: 20px;
    }
    .footer {
        font-size: 15px;
        color: #444;
        margin-top: 22px;
    }
    @media (max-width: 480px) {
        h1 { font-size: 28px; }
        p { font-size: 18px; }
        .button { font-size: 16px; padding: 12px 18px; }
    }
</style>
</head>
<body>
  <div class="container" role="article" aria-label="Confirmação de pagamento">
    <h1>Olá, %s</h1>
    <p>Confirmamos o recebimento do seu pagamento. Agradecemos por utilizar a AgiPay.</p>

    <div class="highlight" role="note" aria-label="Resumo">
      <div class="details">
        <p><strong>Status:</strong> Pago</p>
        <p><strong>Valor:</strong> —</p>
        <p><strong>Data:</strong> —</p>
        <p><strong>Referência:</strong> —</p>
      </div>
    </div>

    <a href="#" class="button" role="button" aria-label="Ver detalhes da transação">Ver detalhes da transação</a>

    <p class="footer">Se precisar de ajuda, responda este e‑mail ou acesse nosso suporte.</p>
  </div>
</body>
</html>
""".formatted(name);

        sendVerificationEmail(to, EmailSubject.PAYMENT_SUCESS.getSubject(), htmlContent);
    }
}
