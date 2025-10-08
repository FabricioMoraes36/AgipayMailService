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

    // Ajuste o tópico abaixo para o mesmo tópico usado pelo produtor (ex.: "payment_order_processed")
    @KafkaListener(topics = "payment_order_processed", groupId = "email-group", containerFactory = "transactionListenerContainerFactory")
    public void consumeMessage(TransactionNotificationDTO dto) throws MessagingException {
        // usa o método específico de payment recebido
        emailService.sendPaymentReceivedEmail(dto.email(), dto.name());
    }
}
