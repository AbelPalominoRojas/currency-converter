package com.abelpalomino.currencyconverter.exchangerates.application.service;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateMediumDto;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Flux<ExchangeRateMediumDto> findAll();

    Mono<ExchangeRateDto> findById(Long id);

    Mono<ExchangeRateDto> create(ExchangeRateBodyDto exchangeRateBody, ServerHttpRequest request);

    Mono<ExchangeRateDto> update(Long id, ExchangeRateBodyDto exchangeRateBody, ServerHttpRequest request);
}
