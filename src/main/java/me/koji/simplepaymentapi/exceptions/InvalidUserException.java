package me.koji.simplepaymentapi.exceptions;

public class InvalidUserException extends FormattedException {
    public InvalidUserException(String message, Object... args) {
        super(message, args);
    }
}
