package com.abelpalomino.currencyconverter.exchangerates.domain.port;

import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRatePort {
    Flux<ExchangeRate> findAll();
    Mono<ExchangeRate> findById(Long id);
    Mono<ExchangeRate> save(ExchangeRate model);
}
