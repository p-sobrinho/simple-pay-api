package me.koji.simplepaymentapi.controller;

import me.koji.simplepaymentapi.dto.ServerInternalErrorDTO;
import me.koji.simplepaymentapi.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    private final int FORBIDDEN_CODE = 403;

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ServerInternalErrorDTO> handleInvalidUser(InvalidUserException exception) {
        return ResponseEntity.internalServerError().body(new ServerInternalErrorDTO(exception.getMessage(), 500));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ServerInternalErrorDTO> handleBadRequest(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ServerInternalErrorDTO(ex.getMessage(), 400));
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<ServerInternalErrorDTO> handleNotEnoughBalance(NotEnoughBalanceException exception) {
        return ResponseEntity.internalServerError().body(new ServerInternalErrorDTO(exception.getMessage(), 500));
    }

    @ExceptionHandler(EqualsUserTransactionException.class)
    public ResponseEntity<ServerInternalErrorDTO> handleEqualsUserTransaction(EqualsUserTransactionException exception) {
        return ResponseEntity.internalServerError().body(new ServerInternalErrorDTO(exception.getMessage(), 500));
    }

    @ExceptionHandler(FailedToAuthenticate.class)
    public ResponseEntity<ServerInternalErrorDTO> handleFailedToAuthenticate(FailedToAuthenticate exception) {
        return ResponseEntity.internalServerError().body(new ServerInternalErrorDTO(exception.getMessage(), 500));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ServerInternalErrorDTO> handleAuthentication(AuthenticationException exception) {
        return ResponseEntity.status(FORBIDDEN_CODE)
                .body(new ServerInternalErrorDTO(exception.getMessage(), FORBIDDEN_CODE));
    }
}
