package me.koji.simplepaymentapi.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ClientTransactionDTO(
        Long id, Long sender, Long receiver,
        String message, BigDecimal value, Instant timestamp
) { }