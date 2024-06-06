package com.abelpalomino.currencyconverter.shared.infrastructure.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthenticationManagerImpl implements ReactiveAuthenticationManager {
    private final JwtHelper jwtHelper;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtHelper.getUserNameFromToken(token);

        if (username == null && !jwtHelper.validateToken(token))
            return Mono.error(new AuthenticationServiceException("Token not valid or expired"));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authentication.getAuthorities()
        );

        return Mono.just(authenticationToken);
    }

}
