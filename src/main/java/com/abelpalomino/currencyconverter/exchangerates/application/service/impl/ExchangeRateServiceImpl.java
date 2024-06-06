package com.abelpalomino.currencyconverter.exchangerates.application.service.impl;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateMediumDto;
import com.abelpalomino.currencyconverter.exchangerates.application.mapper.ExchangeRateDtoMapper;
import com.abelpalomino.currencyconverter.exchangerates.application.service.ExchangeRateService;
import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import com.abelpalomino.currencyconverter.exchangerates.domain.port.ExchangeRatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRatePort exchangeRatePort;
    private final ExchangeRateDtoMapper exchangeRateMapper;

    @Override
    public Flux<ExchangeRateMediumDto> findAll() {
        return exchangeRatePort.findAll()
                .map(exchangeRateMapper::toMediumDto);
    }

    @Override
    public Mono<ExchangeRateDto> findById(Long id) {
        return exchangeRatePort.findById(id)
                .map(exchangeRateMapper::toDto);
    }

    @Override
    public Mono<ExchangeRateDto> create(ExchangeRateBodyDto exchangeRateBody) {
        ExchangeRate exchangeRate = exchangeRateMapper.toEntity(exchangeRateBody);

        exchangeRate.setCreatedAt(LocalDateTime.now());
        calculateDestinationAmount(exchangeRate);


        return exchangeRatePort.save(exchangeRate)
                .map(exchangeRateMapper::toDto);
    }


    @Override
    public Mono<ExchangeRateDto> update(Long id, ExchangeRateBodyDto exchangeRateBody) {
        return exchangeRatePort.findById(id)
                .map(exchangeRate -> buildExchangeRate(exchangeRate, exchangeRateBody))
                .flatMap(exchangeRatePort::save)
                .map(exchangeRateMapper::toDto);
    }

    private void calculateDestinationAmount(ExchangeRate exchangeRate) {
        BigDecimal destinationAmount = exchangeRate.getOriginAmount()
                .multiply(exchangeRate.getExchangeRateAmount());

        exchangeRate.setDestinationAmount(destinationAmount);
    }

    private ExchangeRate buildExchangeRate(ExchangeRate exchangeRate, ExchangeRateBodyDto exchangeRateBody) {
        exchangeRateMapper.updateEntity(exchangeRate, exchangeRateBody);

        exchangeRate.setUpdatedAt(LocalDateTime.now());
        calculateDestinationAmount(exchangeRate);

        return exchangeRate;
    }
}
