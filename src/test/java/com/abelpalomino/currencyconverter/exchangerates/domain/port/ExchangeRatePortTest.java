package com.abelpalomino.currencyconverter.exchangerates.domain.port;

import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.ExchangeRatePortImpl;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.entity.ExchangeRateEntity;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.mapper.ExchangeRateMapperImpl;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ExchangeRatePortTest {

    @Mock
    ExchangeRateRepository exchangeRateRepository;

    @Spy
    ExchangeRateMapperImpl exchangeRateMapper;

    @InjectMocks
    ExchangeRatePortImpl exchangeRatePort;

    @Captor
    ArgumentCaptor<ExchangeRate> exchangeRateCaptor;

    ExchangeRateEntity exchangeRateEntity;
    Long id = 1L;

    @BeforeEach
    void setUp() {
        exchangeRateEntity = ExchangeRateEntity.builder()
                .id(id)
                .originCurrency("USD")
                .originAmount(new BigDecimal("100"))
                .exchangeRateAmount(new BigDecimal("1.5"))
                .destinationCurrency("EUR")
                .destinationAmount(new BigDecimal("150"))
                .build();
    }

    @Test
    @DisplayName("Should return all exchange rates")
    void shouldReturnAllExchangeRates() {
        // Given
        when(exchangeRateRepository.findAll())
                .thenReturn(Flux.just(exchangeRateEntity));

        // When // Then
        StepVerifier.create(exchangeRatePort.findAll())
                .expectNextMatches(exchangeRate -> exchangeRate.getId().equals(id)
                        && exchangeRate.getOriginCurrency().equals(exchangeRateEntity.getOriginCurrency())
                        && exchangeRate.getOriginAmount().equals(exchangeRateEntity.getOriginAmount())
                        && exchangeRate.getExchangeRateAmount().equals(exchangeRateEntity.getExchangeRateAmount())
                        && exchangeRate.getDestinationCurrency().equals(exchangeRateEntity.getDestinationCurrency())
                        && exchangeRate.getDestinationAmount().equals(exchangeRateEntity.getDestinationAmount())
                )
                .verifyComplete();
    }


    @Test
    @DisplayName("Should return exchange rate by id")
    void shouldReturnExchangeRateById() {
        // Given
        when(exchangeRateRepository.findById(anyLong()))
                .thenReturn(Mono.just(exchangeRateEntity));

        // When // Then
        StepVerifier.create(exchangeRatePort.findById(id))
                .expectNextMatches(exchangeRate -> exchangeRate.getId().equals(id)
                        && exchangeRate.getOriginCurrency().equals(exchangeRateEntity.getOriginCurrency())
                        && exchangeRate.getOriginAmount().equals(exchangeRateEntity.getOriginAmount())
                        && exchangeRate.getExchangeRateAmount().equals(exchangeRateEntity.getExchangeRateAmount())
                        && exchangeRate.getDestinationCurrency().equals(exchangeRateEntity.getDestinationCurrency())
                        && exchangeRate.getDestinationAmount().equals(exchangeRateEntity.getDestinationAmount())
                )
                .verifyComplete();

    }

    @Test
    @DisplayName("Should return empty when exchange rate not found by id")
    void shouldReturnEmptyWhenExchangeRateNotFoundById() {
        // Given
        when(exchangeRateRepository.findById(1L))
                .thenReturn(Mono.empty());

        // When // Then
        StepVerifier.create(exchangeRatePort.findById(id))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should save and return exchange rate")
    void shouldSaveAndReturnExchangeRate() {
        // Given
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .id(id)
                .originCurrency("USD")
                .originAmount(new BigDecimal("100"))
                .exchangeRateAmount(new BigDecimal("1.5"))
                .destinationCurrency("EUR")
                .destinationAmount(new BigDecimal("150"))
                .build();

        when(exchangeRateRepository.save(any(ExchangeRateEntity.class)))
                .thenAnswer(invocation -> {
                    ExchangeRateEntity exchangeRateEntity = invocation.getArgument(0);
                    exchangeRateEntity.setId(id);
                    return Mono.just(exchangeRateEntity);
                });

        // When // Then
        StepVerifier.create(exchangeRatePort.save(exchangeRate))
                .expectNextMatches(savedExchangeRate -> savedExchangeRate.getId().equals(id)
                        && savedExchangeRate.getOriginCurrency().equals(exchangeRate.getOriginCurrency())
                        && savedExchangeRate.getOriginAmount().equals(exchangeRate.getOriginAmount())
                        && savedExchangeRate.getExchangeRateAmount().equals(exchangeRate.getExchangeRateAmount())
                        && savedExchangeRate.getDestinationCurrency().equals(exchangeRate.getDestinationCurrency())
                        && savedExchangeRate.getDestinationAmount().equals(exchangeRate.getDestinationAmount())
                )
                .verifyComplete();

    }

}