package com.abelpalomino.currencyconverter.users.infrastructure.persistence.repository;

import com.abelpalomino.currencyconverter.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<UserEntity> findByEmail(String email);
}
