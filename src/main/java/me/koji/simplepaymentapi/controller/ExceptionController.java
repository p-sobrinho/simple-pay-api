package me.koji.simplepaymentapi.controller;

import me.koji.simplepaymentapi.dto.ServerInternalErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerInternalErrorDTO> handleException(IllegalArgumentException exception) {
        return ResponseEntity.internalServerError().body(new ServerInternalErrorDTO(exception.getMessage(), 500));
    }
}
