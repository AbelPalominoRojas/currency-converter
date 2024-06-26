package com.abelpalomino.currencyconverter.shared.infrastructure.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeExchange(request ->
                        request
                                .pathMatchers(HttpMethod.POST, "/users").permitAll()
                                .pathMatchers(
                                        "/v3/api-docs/**",
                                        "/webjars/swagger-ui/**",
                                        "/login"
                                )
                                .permitAll()
                                .anyExchange()
                                .authenticated()
                )
                .exceptionHandling(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .build();
    }


}
