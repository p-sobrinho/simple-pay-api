package me.koji.simplepaymentapi.exceptions;

public class EqualsUserTransactionException extends FormattedException {
    public EqualsUserTransactionException(String message, Object... args) {
        super(message, args);
    }
}