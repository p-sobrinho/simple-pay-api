package me.koji.simplepaymentapi.exceptions;

public class FailedToAuthenticate extends FormattedException {
    public FailedToAuthenticate(String message, Object... args) {
        super(message, args);
    }
}
