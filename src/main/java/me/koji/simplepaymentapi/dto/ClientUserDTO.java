package me.koji.simplepaymentapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.koji.simplepaymentapi.types.ClientUserType;

import java.math.BigDecimal;

public record ClientUserDTO(
        Long id,
        @NotBlank(message = "First name is required.")
        String firstName,
        @NotBlank(message = "Last name is required.")
        String lastName,
        @Email(message = "Email must be valid.")
        @NotBlank(message = "Email is required.")
        String email,
        @NotBlank(message = "CPF is required.")
        @Size(min = 11, max = 11, message = "CPF must have 11 digits.")
        String cpf,
        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 32, message = "Password size must be equals or greater than 8.")
        String password,
        BigDecimal balance, ClientUserType type
){ }
