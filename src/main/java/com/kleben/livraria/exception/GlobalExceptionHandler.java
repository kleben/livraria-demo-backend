package com.kleben.livraria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder validationErrors = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.append(error.getDefaultMessage());
        }

        ProblemDetails problem = ProblemDetails.create(
            "Requisição inválida",
            HttpStatus.BAD_REQUEST.value(),
            "Houve erros de validação nos dados enviados.",
            null
        );

        if (!validationErrors.isEmpty())
            problem.setDetail(validationErrors.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetails> handleGenericException(RuntimeException ex) {
        ProblemDetails problem = ProblemDetails.create(
            "Erro interno no servidor",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getMessage(),
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
