package com.AgiPay.MailService.AgiPayMailService.Service;

import com.AgiPay.MailService.AgiPayMailService.DTO.TransactionNotificationDTO;
import jakarta.mail.MessagingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final EmailService emailService;

    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }
    // Consome emails de pagamento
    @KafkaListener(topics = "payment_order_processed", groupId = "email-group", containerFactory = "transactionListenerContainerFactory")
    public void consumePayment(TransactionNotificationDTO dto) throws MessagingException {
        emailService.sendPaymentReceivedEmail(dto.email(), dto.name());
    }

    // Consome emails de boas-vindas
    @KafkaListener(topics = "welcome_email", groupId = "email-group", containerFactory = "transactionListenerContainerFactory")
    public void consumeWelcome(TransactionNotificationDTO dto) throws MessagingException {
        emailService.sendWelcomeEmail(dto.email(), dto.name());
    }
    // Consome emails de sucesso de pagamento
    @KafkaListener(topics = "payment_success", groupId = "email-group", containerFactory = "transactionListenerContainerFactory")
    public void consumePaymentSuccess(TransactionNotificationDTO dto) throws MessagingException {
        emailService.sendPaymentSuccessEmail(dto.email(), dto.name());
    }
}
