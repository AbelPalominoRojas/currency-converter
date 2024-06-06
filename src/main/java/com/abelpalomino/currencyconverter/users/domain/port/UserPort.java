package com.abelpalomino.currencyconverter.users.domain.port;

import com.abelpalomino.currencyconverter.users.domain.model.UserModel;
import reactor.core.publisher.Mono;

public interface UserPort {

    Mono<UserModel> save(UserModel model);

    Mono<UserModel> findByEmail(String email);
}
