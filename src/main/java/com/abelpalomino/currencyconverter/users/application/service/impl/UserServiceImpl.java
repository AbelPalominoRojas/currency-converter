package com.abelpalomino.currencyconverter.users.application.service.impl;

import com.abelpalomino.currencyconverter.shared.application.dto.exception.DataNotFoundException;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.UniqueConstraintException;
import com.abelpalomino.currencyconverter.shared.infrastructure.web.security.JwtHelper;
import com.abelpalomino.currencyconverter.users.application.dto.user.AuthDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserBodyDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserSecurityDto;
import com.abelpalomino.currencyconverter.users.application.mapper.UserDtoMapper;
import com.abelpalomino.currencyconverter.users.application.service.UserService;
import com.abelpalomino.currencyconverter.users.domain.model.UserModel;
import com.abelpalomino.currencyconverter.users.domain.port.UserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserPort userPort;
    private final UserDtoMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Override
    public Mono<UserDto> create(UserBodyDto userBody) {
        UserModel user = userMapper.toModel(userBody);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        log.info("Password: {}", user.getPassword());
        return userPort.findByEmail(user.getEmail())
                .flatMap(u -> {
                    if (u != null) {
                        return Mono.error(
                                new UniqueConstraintException("User already exists with email: " + user.getEmail())
                        );
                    }

                    return saveUser(user);
                })
                .switchIfEmpty(saveUser(user));
    }

    private Mono<UserDto> saveUser(UserModel user) {
        return userPort.save(user).map(userMapper::toDto);
    }

    @Override
    public Mono<UserDto> findByEmail(String email) {
        return userPort.findByEmail(email)
                .map(userMapper::toDto)
                .switchIfEmpty(Mono.error(new DataNotFoundException("User not found with email: " + email)));
    }

    @Override
    public Mono<UserSecurityDto> login(AuthDto authDto) {

        return userPort.findByEmail(authDto.getEmail())
                .switchIfEmpty(Mono.error(new DataNotFoundException("User not found with email: " + authDto.getEmail())))
                .filter(user -> passwordEncoder.matches(authDto.getPassword(), user.getPassword()))
                .switchIfEmpty(Mono.error(new DataNotFoundException("User/Password are not correct")))
                .map(buildUserSecurity());
    }

    private Function<UserModel, UserSecurityDto> buildUserSecurity() {
        return user -> {
            var authorities = List.of(new SimpleGrantedAuthority("DEFAULT"));
            var userDetails = new User(user.getEmail(), user.getPassword(), authorities);

            UserSecurityDto userSecurity = userMapper.toSecurityDto(user);
            userSecurity.setAccessToken(jwtHelper.generateToken(userDetails));

            return userSecurity;
        };
    }
}
