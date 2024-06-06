package com.abelpalomino.currencyconverter.users.application.service;

import com.abelpalomino.currencyconverter.users.application.dto.user.AuthDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserBodyDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserSecurityDto;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> create(UserBodyDto userBody);

    Mono<UserDto> findByEmail(String email);

    Mono<UserSecurityDto> login(AuthDto authDto);
}
