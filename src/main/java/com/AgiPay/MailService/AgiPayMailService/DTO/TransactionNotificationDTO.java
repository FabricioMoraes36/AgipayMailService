package com.AgiPay.MailService.AgiPayMailService.DTO;

import com.AgiPay.MailService.AgiPayMailService.Enums.EmailSubject;

public record TransactionNotificationDTO(String name, String email, EmailSubject type) {
}
