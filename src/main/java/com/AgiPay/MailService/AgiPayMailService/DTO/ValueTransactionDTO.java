package com.AgiPay.MailService.AgiPayMailService.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ValueTransactionDTO(String email, BigDecimal value, LocalDateTime date) {
}
