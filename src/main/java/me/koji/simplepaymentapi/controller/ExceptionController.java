package me.koji.simplepaymentapi.controller;

import me.koji.simplepaymentapi.dto.ServerInternalErrorDTO;
import me.koji.simplepaymentapi.exceptions.InvalidUserException;
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
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerInternalErrorDTO> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ServerInternalErrorDTO(exception.getMessage(), 400));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ServerInternalErrorDTO> handleInvalidUser(InvalidUserException exception) {
        return ResponseEntity.internalServerError().body(new ServerInternalErrorDTO(exception.getMessage(), 500));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
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
}
