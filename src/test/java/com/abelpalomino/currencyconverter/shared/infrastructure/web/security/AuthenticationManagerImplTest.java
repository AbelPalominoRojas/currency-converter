package com.abelpalomino.currencyconverter.shared.infrastructure.web.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationManagerImplTest {
    @Mock
    JwtHelper jwtHelper;

    @InjectMocks
    AuthenticationManagerImpl authenticationManager;


    @Test
    void authenticateShouldReturnValidAuthenticationWhenTokenIsValid() {
        // Given
        String token = "validToken";
        String username = "validUsername";
        var authorities = List.of(new SimpleGrantedAuthority("DEFAULT"));


        Authentication authentication = new UsernamePasswordAuthenticationToken(token, new Object(), authorities);

        when(jwtHelper.getUserNameFromToken(anyString())).thenReturn(username);


        // When // Then
        StepVerifier.create(authenticationManager.authenticate(authentication))
                .expectNextMatches(auth -> auth.getName().equals(username))
                .verifyComplete();
    }

    @Test
    void authenticateShouldThrowExceptionWhenTokenIsInvalid() {
        // Given
        String token = "invalidToken";
        var authorities = List.of(new SimpleGrantedAuthority("DEFAULT"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(token, new Object(), authorities);

        when(jwtHelper.getUserNameFromToken(anyString())).thenReturn(null);
        when(jwtHelper.validateToken(anyString())).thenReturn(false);


        // When // Then
        StepVerifier.create(authenticationManager.authenticate(authentication))
                .expectError(AuthenticationServiceException.class)
                .verify();
    }
}