package com.abelpalomino.currencyconverter.shared.infrastructure.web.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerSecurityContextRepositoryImplTest {

    @Mock
    ReactiveAuthenticationManager authenticationManager;

    @InjectMocks
    ServerSecurityContextRepositoryImpl securityContextRepository;


    @Test
    void loadShouldReturnSecurityContextWhenTokenIsValid() {
        // Given
        String token = "validToken";
        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(auth));

        // When // Then
        StepVerifier.create(securityContextRepository.load(exchange))
                .expectNextMatches(context -> context.getAuthentication().equals(auth))
                .verifyComplete();
    }

    @Test
    void loadShouldThrowExceptionWhenTokenIsInvalid() {
        // Given
        String token = "invalidToken";
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(authenticationManager.authenticate(any()))
                .thenReturn(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")));

        // When // Then
        StepVerifier.create(securityContextRepository.load(exchange))
                .expectError(ResponseStatusException.class)
                .verify();
    }
}