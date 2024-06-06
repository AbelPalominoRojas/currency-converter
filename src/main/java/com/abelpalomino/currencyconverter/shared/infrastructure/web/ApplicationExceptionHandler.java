package com.abelpalomino.currencyconverter.shared.infrastructure.web;

import com.abelpalomino.currencyconverter.shared.application.dto.exception.ArgumentNotValidError;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.DataNotFoundException;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.GeneralError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ArgumentNotValidError>> handleInvalidArgument(WebExchangeBindException ex) {
        ArgumentNotValidError response = new ArgumentNotValidError();
        Map<String, String> error = new HashMap<>();


        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    error.put(fieldError.getField(), fieldError.getDefaultMessage());
                });

        response.setMessage("Invalid arguments");
        response.setError(error);

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public Mono<ResponseEntity<GeneralError>> handleDataNotFound(DataNotFoundException ex) {
        GeneralError response = new GeneralError();

        response.setMessage(ex.getMessage());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response));
    }
}
