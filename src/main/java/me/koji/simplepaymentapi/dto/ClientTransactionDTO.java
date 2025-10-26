package me.koji.simplepaymentapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record ClientTransactionDTO(
        Long id,
        @NotNull(message = "Sender is required.")
        Long sender,
        @NotNull(message = "Receiver is required.")
        Long receiver,
        String message,
        @NotNull(message = "Value is required.")
        BigDecimal value,
        Instant timestamp
) { }