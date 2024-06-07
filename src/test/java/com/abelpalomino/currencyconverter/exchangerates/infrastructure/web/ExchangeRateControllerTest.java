package com.abelpalomino.currencyconverter.exchangerates.infrastructure.web;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.entity.ExchangeRateEntity;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class ExchangeRateControllerTest {
    @Autowired
    private WebTestClient client;

    @Autowired
    ExchangeRateController exchangeRateController;

    @MockBean
    ExchangeRateRepository exchangeRateRepository;

    @Captor
    ArgumentCaptor<ExchangeRateBodyDto> exchangeRateBodyCaptor;

    ExchangeRateEntity exchangeRateEntity;
    Long id = 1L;

    ExchangeRateBodyDto exchangeRateBody;

    @BeforeEach
    void setUp() {
        client = WebTestClient
                .bindToController(exchangeRateController)
                .apply(springSecurity())
                .configureClient()
                .apply(csrf())
                .build();

        exchangeRateEntity = ExchangeRateEntity.builder()
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
    @DisplayName("find All should return all exchange rates")
    void findAllShouldReturnAllExchangeRates() {
        // Given
        when(exchangeRateRepository.findAll())
                .thenReturn(Flux.just(exchangeRateEntity));

        // When // Then
        client.get()
                .uri("/exchange-rates")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRateEntity.class)
                .hasSize(1);
    }


    @Test
    @DisplayName("find By Id should return exchange rate when id exists")
    void findByIdShouldReturnExchangeRateWhenIdExists() {
        // Given
        when(exchangeRateRepository.findById(id))
                .thenReturn(Mono.just(exchangeRateEntity));

        // When // Then
        client.get()
                .uri("/exchange-rates/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(exchangeRateEntity.getId())
                .jsonPath("$.originCurrency").isEqualTo(exchangeRateEntity.getOriginCurrency())
                .jsonPath("$.originAmount").isEqualTo(exchangeRateEntity.getOriginAmount())
                .jsonPath("$.exchangeRateAmount").isEqualTo(exchangeRateEntity.getExchangeRateAmount())
                .jsonPath("$.destinationCurrency").isEqualTo(exchangeRateEntity.getDestinationCurrency())
                .jsonPath("$.destinationAmount").isEqualTo(exchangeRateEntity.getDestinationAmount());

    }


    @Test
    @DisplayName("create should return created exchange rate in db")
    void createShouldReturnCreatedExchangeRate() {
        // Given
        when(exchangeRateRepository.save(any(ExchangeRateEntity.class)))
                .thenAnswer(invocation -> {
                    ExchangeRateEntity exchangeRateEntity = invocation.getArgument(0);
                    exchangeRateEntity.setId(id);
                    return Mono.just(exchangeRateEntity);
                });

        // When // Then
        BigDecimal destinationAmount = exchangeRateBody.getOriginAmount()
                .multiply(exchangeRateBody.getExchangeRateAmount());

        client.post()
                .uri("/exchange-rates")
                .bodyValue(exchangeRateBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.originCurrency").isEqualTo(exchangeRateBody.getOriginCurrency())
                .jsonPath("$.originAmount").isEqualTo(exchangeRateBody.getOriginAmount())
                .jsonPath("$.exchangeRateAmount").isEqualTo(exchangeRateBody.getExchangeRateAmount())
                .jsonPath("$.destinationCurrency").isEqualTo(exchangeRateBody.getDestinationCurrency())
                .jsonPath("$.destinationAmount").isEqualTo(destinationAmount);
    }

    @Test
    @DisplayName("update should return updated exchange rate when id exists in db")
    void updateShouldReturnUpdatedExchangeRateWhenIdExists() {
        // Given
        when(exchangeRateRepository.findById(id))
                .thenReturn(Mono.just(exchangeRateEntity));

        when(exchangeRateRepository.save(any(ExchangeRateEntity.class)))
                .thenAnswer(invocation -> {
                    ExchangeRateEntity exchangeRateEntity = invocation.getArgument(0);
                    exchangeRateEntity.setId(id);
                    return Mono.just(exchangeRateEntity);
                });

        // When // Then
        BigDecimal destinationAmount = exchangeRateBody.getOriginAmount()
                .multiply(exchangeRateBody.getExchangeRateAmount());

        client.put()
                .uri("/exchange-rates/" + id)
                .bodyValue(exchangeRateBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.originCurrency").isEqualTo(exchangeRateBody.getOriginCurrency())
                .jsonPath("$.originAmount").isEqualTo(exchangeRateBody.getOriginAmount())
                .jsonPath("$.exchangeRateAmount").isEqualTo(exchangeRateBody.getExchangeRateAmount())
                .jsonPath("$.destinationCurrency").isEqualTo(exchangeRateBody.getDestinationCurrency())
                .jsonPath("$.destinationAmount").isEqualTo(destinationAmount);
    }

}