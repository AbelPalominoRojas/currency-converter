package com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.repository;

import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.entity.ExchangeRateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRateEntity, Long> {
}
