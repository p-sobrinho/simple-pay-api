package me.koji.simplepaymentapi.controller;

import me.koji.simplepaymentapi.dto.ServerInternalErrorDTO;
import me.koji.simplepaymentapi.exceptions.InvalidUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
