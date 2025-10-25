package me.koji.simplepaymentapi.exceptions;

import java.text.MessageFormat;

public class FormattedException extends RuntimeException {
    public FormattedException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }
}