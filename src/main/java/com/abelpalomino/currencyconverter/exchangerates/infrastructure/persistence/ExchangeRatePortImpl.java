package com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence;

import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import com.abelpalomino.currencyconverter.exchangerates.domain.port.ExchangeRatePort;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.entity.ExchangeRateEntity;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.mapper.ExchangeRateMapper;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class ExchangeRatePortImpl implements ExchangeRatePort {
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    @Override
    public Flux<ExchangeRate> findAll() {
        return exchangeRateRepository.findAll()
                .map(exchangeRateMapper::toModel);
    }

    @Override
    public Mono<ExchangeRate> findById(Long id) {
        return exchangeRateRepository.findById(id)
                .map(exchangeRateMapper::toModel);
    }

    @Override
    public Mono<ExchangeRate> save(ExchangeRate model) {
        ExchangeRateEntity exchangeRate = exchangeRateMapper.toEntity(model);

        return exchangeRateRepository.save(exchangeRate)
                .map(exchangeRateMapper::toModel);
    }
}
