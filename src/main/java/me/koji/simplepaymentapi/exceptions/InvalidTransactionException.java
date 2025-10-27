package me.koji.simplepaymentapi.exceptions;

public class InvalidTransactionException extends FormattedException {
    public InvalidTransactionException(String message, Object... args) {
        super(message, args);
    }
}

