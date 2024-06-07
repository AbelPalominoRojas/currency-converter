package com.abelpalomino.currencyconverter.exchangerates.application.service;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.application.mapper.ExchangeRateDtoMapperImpl;
import com.abelpalomino.currencyconverter.exchangerates.application.service.impl.ExchangeRateServiceImpl;
import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import com.abelpalomino.currencyconverter.exchangerates.domain.port.ExchangeRatePort;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.DataNotFoundException;
import com.abelpalomino.currencyconverter.shared.infrastructure.web.security.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {
    @Mock
    ExchangeRatePort exchangeRatePort;

    @Mock
    JwtHelper jwtHelper;

    @Mock
    private ServerHttpRequest serverHttpRequest;

    @Spy
    ExchangeRateDtoMapperImpl exchangeRateMapper;

    @InjectMocks
    ExchangeRateServiceImpl exchangeRateService;

    @Captor
    ArgumentCaptor<ExchangeRateBodyDto> exchangeRateBodyCaptor;

    ExchangeRate exchangeRate;
    Long id = 1L;

    ExchangeRateBodyDto exchangeRateBody;

    @BeforeEach
    void setUp() {
        exchangeRate = ExchangeRate.builder()
                .id(id)
                .originCurrency("USD")
                .originAmount(new BigDecimal("100"))
                .exchangeRateAmount(new BigDecimal("1.5"))
                .destinationCurrency("EUR")
                .destinationAmount(new BigDecimal("150"))
                .build();

        exchangeRateBody = ExchangeRateBodyDto.builder()
                .originCurrency("USD")
                .originAmount(new BigDecimal("100"))
                .exchangeRateAmount(new BigDecimal("1.5"))
                .destinationCurrency("EUR")
                .build();
    }

    @Test
    @DisplayName("findAll should return all exchange rates")
    void findAllShouldReturnAllExchangeRates() {
        // Given
        when(exchangeRatePort.findAll())
                .thenReturn(Flux.just(exchangeRate));

        // When // Then
        StepVerifier.create(exchangeRateService.findAll())
                .expectNextMatches(exchangeRateDto -> {
                    assertEquals(id, exchangeRateDto.getId());
                    assertEquals(exchangeRateDto.getOriginCurrency(), exchangeRate.getOriginCurrency());
                    assertEquals(exchangeRateDto.getOriginAmount(), exchangeRate.getOriginAmount());
                    assertEquals(exchangeRateDto.getExchangeRateAmount(), exchangeRate.getExchangeRateAmount());
                    assertEquals(exchangeRateDto.getDestinationCurrency(), exchangeRate.getDestinationCurrency());
                    assertEquals(exchangeRateDto.getDestinationAmount(), exchangeRate.getDestinationAmount());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    @DisplayName("findById should return exchange rate")
    void findByIdShouldReturnExchangeRate() {
        // Given
        when(exchangeRatePort.findById(anyLong()))
                .thenReturn(Mono.just(exchangeRate));

        // When // Then
        StepVerifier.create(exchangeRateService.findById(id))
                .expectNextMatches(exchangeRateDto -> {
                    assertEquals(id, exchangeRateDto.getId());
                    assertEquals(exchangeRateDto.getOriginCurrency(), exchangeRate.getOriginCurrency());
                    assertEquals(exchangeRateDto.getOriginAmount(), exchangeRate.getOriginAmount());
                    assertEquals(exchangeRateDto.getExchangeRateAmount(), exchangeRate.getExchangeRateAmount());
                    assertEquals(exchangeRateDto.getDestinationCurrency(), exchangeRate.getDestinationCurrency());
                    assertEquals(exchangeRateDto.getDestinationAmount(), exchangeRate.getDestinationAmount());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("findById should return error when exchange rate not found")
    void findByIdShouldReturnErrorWhenExchangeRateNotFound() {
        // Given
        when(exchangeRatePort.findById(anyLong()))
                .thenReturn(Mono.empty());

        // When // Then
        StepVerifier.create(exchangeRateService.findById(id))
                .expectError(DataNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("create should return created exchange rate")
    void createShouldReturnCreatedExchangeRate() {
        // Given
        when(exchangeRatePort.save(any(ExchangeRate.class)))
                .thenAnswer(invocation -> {
                    ExchangeRate exchangeRate = invocation.getArgument(0);
                    exchangeRate.setId(id);
                    return Mono.just(exchangeRate);
                });

        when(serverHttpRequest.getHeaders()).thenReturn(new HttpHeaders());

        // When // Then
        StepVerifier.create(exchangeRateService.create(exchangeRateBody, serverHttpRequest))
                .expectNextMatches(exchangeRateDto -> {
                    assertEquals(id, exchangeRateDto.getId());
                    assertEquals(exchangeRateDto.getOriginCurrency(), exchangeRateBody.getOriginCurrency());
                    assertEquals(exchangeRateDto.getOriginAmount(), exchangeRateBody.getOriginAmount());
                    assertEquals(exchangeRateDto.getExchangeRateAmount(), exchangeRateBody.getExchangeRateAmount());
                    assertEquals(exchangeRateDto.getDestinationCurrency(), exchangeRateBody.getDestinationCurrency());
                    return true;
                })
                .verifyComplete();

    }

    @Test
    @DisplayName("update should return updated exchange rate")
    void updateShouldReturnUpdatedExchangeRate() {
        // Given
        when(exchangeRatePort.findById(anyLong()))
                .thenReturn(Mono.just(exchangeRate));

        when(exchangeRatePort.save(any(ExchangeRate.class)))
                .thenAnswer(invocation -> {
                    ExchangeRate exchangeRate = invocation.getArgument(0);
                    exchangeRate.setId(id);
                    return Mono.just(exchangeRate);
                });

        when(serverHttpRequest.getHeaders()).thenReturn(new HttpHeaders());

        // When // Then
        StepVerifier.create(exchangeRateService.update(id, exchangeRateBody, serverHttpRequest))
                .expectNextMatches(exchangeRateDto -> {
                    assertEquals(id, exchangeRateDto.getId());
                    assertEquals(exchangeRateDto.getOriginCurrency(), exchangeRateBody.getOriginCurrency());
                    assertEquals(exchangeRateDto.getOriginAmount(), exchangeRateBody.getOriginAmount());
                    assertEquals(exchangeRateDto.getExchangeRateAmount(), exchangeRateBody.getExchangeRateAmount());
                    assertEquals(exchangeRateDto.getDestinationCurrency(), exchangeRateBody.getDestinationCurrency());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("update should return error when exchange rate not found")
    void updateShouldReturnErrorWhenExchangeRateNotFound() {
        // Given
        when(exchangeRatePort.findById(anyLong()))
                .thenReturn(Mono.empty());

        // When // Then
        StepVerifier.create(exchangeRateService.update(id, exchangeRateBody, serverHttpRequest))
                .expectError(DataNotFoundException.class)
                .verify();
    }

}