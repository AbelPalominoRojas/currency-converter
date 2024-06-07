package com.abelpalomino.currencyconverter.exchangerates.application.service.impl;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateMediumDto;
import com.abelpalomino.currencyconverter.exchangerates.application.mapper.ExchangeRateDtoMapper;
import com.abelpalomino.currencyconverter.exchangerates.application.service.ExchangeRateService;
import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import com.abelpalomino.currencyconverter.exchangerates.domain.port.ExchangeRatePort;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.DataNotFoundException;
import com.abelpalomino.currencyconverter.shared.infrastructure.web.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
    private final JwtHelper jwtHelper;

    @Override
    public Flux<ExchangeRateMediumDto> findAll() {
        return exchangeRatePort.findAll()
                .map(exchangeRateMapper::toMediumDto);
    }

    @Override
    public Mono<ExchangeRateDto> findById(Long id) {
        return exchangeRatePort.findById(id)
                .map(exchangeRateMapper::toDto)
                .switchIfEmpty(buildNotFoundError(id));
    }

    @Override
    public Mono<ExchangeRateDto> create(ExchangeRateBodyDto exchangeRateBody, ServerHttpRequest request) {
        ExchangeRate exchangeRate = exchangeRateMapper.toEntity(exchangeRateBody);

        exchangeRate.setCreatedAt(LocalDateTime.now());
        exchangeRate.setCreatedBy(buildUsername(request));
        calculateDestinationAmount(exchangeRate);


        return exchangeRatePort.save(exchangeRate)
                .map(exchangeRateMapper::toDto);
    }


    @Override
    public Mono<ExchangeRateDto> update(Long id, ExchangeRateBodyDto exchangeRateBody, ServerHttpRequest request) {
        return exchangeRatePort.findById(id)
                .map(exchangeRate -> buildExchangeRate(exchangeRate, exchangeRateBody, request))
                .flatMap(exchangeRatePort::save)
                .map(exchangeRateMapper::toDto)
                .switchIfEmpty(buildNotFoundError(id));
    }

    private void calculateDestinationAmount(ExchangeRate exchangeRate) {
        BigDecimal destinationAmount = exchangeRate.getOriginAmount()
                .multiply(exchangeRate.getExchangeRateAmount());

        exchangeRate.setDestinationAmount(destinationAmount);
    }

    private ExchangeRate buildExchangeRate(
            ExchangeRate exchangeRate,
            ExchangeRateBodyDto exchangeRateBody,
            ServerHttpRequest request) {
        exchangeRateMapper.updateEntity(exchangeRate, exchangeRateBody);

        exchangeRate.setUpdatedAt(LocalDateTime.now());
        exchangeRate.setUpdatedBy(buildUsername(request));
        calculateDestinationAmount(exchangeRate);

        return exchangeRate;
    }

    private static Mono<ExchangeRateDto> buildNotFoundError(Long id) {
        return Mono.error(new DataNotFoundException("Exchange rate not found for id: " + id));
    }

    private String buildUsername(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            String authToken = authHeader.substring(7);

            return jwtHelper.getUserNameFromToken(authToken);
        }

        return null;
    }
}
