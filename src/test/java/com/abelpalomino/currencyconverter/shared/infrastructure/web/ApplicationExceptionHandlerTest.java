package com.abelpalomino.currencyconverter.shared.infrastructure.web;

import com.abelpalomino.currencyconverter.shared.application.dto.exception.DataNotFoundException;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.GeneralError;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.UniqueConstraintException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationExceptionHandlerTest {

    @InjectMocks
    private ApplicationExceptionHandler applicationExceptionHandler;

    @Test
    void shouldHandleDataNotFound() {
        DataNotFoundException ex = mock(DataNotFoundException.class);
        when(ex.getMessage()).thenReturn("Data not found");

        Mono<ResponseEntity<GeneralError>> result = applicationExceptionHandler.handleDataNotFound(ex);

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                    assertEquals("Data not found", responseEntity.getBody().getMessage());
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleUniqueConstraint() {
        UniqueConstraintException ex = mock(UniqueConstraintException.class);
        when(ex.getMessage()).thenReturn("Unique constraint violation");

        Mono<ResponseEntity<GeneralError>> result = applicationExceptionHandler.handleDataNotFound(ex);

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
                    assertEquals("Unique constraint violation", responseEntity.getBody().getMessage());
                })
                .verifyComplete();
    }
}