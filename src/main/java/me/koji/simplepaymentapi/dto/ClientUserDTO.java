package me.koji.simplepaymentapi.dto;

import me.koji.simplepaymentapi.types.ClientUserType;

import java.math.BigDecimal;

public record ClientUserDTO(
        Long id, String firstName, String lastName,
        String email, String cpf, String password,
        BigDecimal balance, ClientUserType type
){ }
