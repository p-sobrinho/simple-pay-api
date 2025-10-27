package me.koji.simplepaymentapi.exceptions;

public class AuthenticationException extends FormattedException {
    public AuthenticationException(String message, Object... args) {
        super(message, args);
    }
}
