package com.abelpalomino.currencyconverter.exchangerates.infrastructure.web;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateMediumDto;
import com.abelpalomino.currencyconverter.exchangerates.application.service.ExchangeRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exchange-rates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public Mono<ResponseEntity<List<ExchangeRateMediumDto>>> findAll() {
        return exchangeRateService.findAll()
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ExchangeRateDto>> findById(@PathVariable("id") Long id) {
        return exchangeRateService.findById(id)
                .map(ResponseEntity::ok);
    }


    @PostMapping
    public Mono<ResponseEntity<ExchangeRateDto>> create(@Valid @RequestBody ExchangeRateBodyDto exchangeRateBody) {
        return exchangeRateService.create(exchangeRateBody)
                .map(exchangeRate -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(exchangeRate)
                );

    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ExchangeRateDto>> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ExchangeRateBodyDto exchangeRateBody) {
        return exchangeRateService.update(id, exchangeRateBody)
                .map(ResponseEntity::ok);
    }


}
