package com.abelpalomino.currencyconverter.users.infrastructure.persistence;

import com.abelpalomino.currencyconverter.users.domain.model.UserModel;
import com.abelpalomino.currencyconverter.users.domain.port.UserPort;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.mapper.UserMapper;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class UserPortImpl implements UserPort {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public Mono<UserModel> save(UserModel model) {
        return userRepository
                .save(userMapper.toEntity(model))
                .map(userMapper::toModel);
    }

    @Override
    public Mono<UserModel> findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(userMapper::toModel);
    }
}
