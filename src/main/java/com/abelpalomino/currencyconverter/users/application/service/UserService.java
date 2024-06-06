package com.abelpalomino.currencyconverter.users.application.service;

import com.abelpalomino.currencyconverter.users.application.dto.user.UserBodyDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserDto;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> create(UserBodyDto userBody);

    Mono<UserDto> findByEmail(String email);
}
