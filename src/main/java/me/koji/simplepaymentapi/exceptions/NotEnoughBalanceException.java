package me.koji.simplepaymentapi.exceptions;

public class NotEnoughBalanceException extends FormattedException {
    public NotEnoughBalanceException(String message, Object... args) {
        super(message, args);
    }
}
