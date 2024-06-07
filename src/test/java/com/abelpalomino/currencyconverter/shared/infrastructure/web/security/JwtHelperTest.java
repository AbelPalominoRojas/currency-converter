package com.abelpalomino.currencyconverter.shared.infrastructure.web.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtHelperTest {
    @InjectMocks
    private JwtHelper jwtHelper;

    UserDetails user;

    @BeforeEach
    void setUp() {
        user = new User("username", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        List<Field> fields = Arrays.asList(jwtHelper.getClass().getDeclaredFields());

        fields.stream()
                .filter(field -> field.getName().equals("secretKey"))
                .findFirst()
                .ifPresent(field -> {
                    String secretKey = "kbNkRsLhHBmm78G1yu73Tyo4xv+UrcclXC174MDiqvsT73NqwF6aKAlbcdubCTv1";
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, jwtHelper, secretKey);
                });

        fields.stream()
                .filter(field -> field.getName().equals("expirationMinutes"))
                .findFirst()
                .ifPresent(field -> {
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, jwtHelper, 60);
                });

    }


    @Test
    void generateTokenShouldReturnValidToken() {
        String token = jwtHelper.generateToken(user);
        assertNotNull(token);
    }

    @Test
    void getClaimsFromTokenShouldReturnValidClaims() {
        String token = jwtHelper.generateToken(user);
        Claims claims = jwtHelper.getClaimsFromToken(token);
        assertEquals("username", claims.getSubject());
    }

    @Test
    void getUserNameFromTokenShouldReturnValidUsername() {
        String token = jwtHelper.generateToken(user);
        String username = jwtHelper.getUserNameFromToken(token);
        assertEquals("username", username);
    }

    @Test
    void getExpirationDateFromTokenShouldReturnValidDate() {
        String token = jwtHelper.generateToken(user);
        Date expirationDate = jwtHelper.getExpirationDateFromToken(token);
        assertNotNull(expirationDate);
    }

    @Test
    void isTokenExpiredShouldReturnFalseForValidToken() {
        String token = jwtHelper.generateToken(user);
        boolean isExpired = jwtHelper.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void validateTokenShouldReturnTrueForValidToken() {
        String token = jwtHelper.generateToken(user);
        boolean isValid = jwtHelper.validateToken(token);
        assertTrue(isValid);
    }
}