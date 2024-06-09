package com.abelpalomino.currencyconverter.shared.infrastructure.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtHelper {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-minutes}")
    private Integer expirationMinutes;


    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("profiles", user.getAuthorities());
        claims.put("username", user.getUsername());

        Date createDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expirationMinutes);

        SecretKey key = getSecretKey();

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(createDate)
                .expiration(calendar.getTime())
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        SecretKey key = getSecretKey();

            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserNameFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private SecretKey getSecretKey(){
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
