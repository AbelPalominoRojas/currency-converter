package com.abelpalomino.currencyconverter.exchangerates.infrastructure.web;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateMediumDto;
import com.abelpalomino.currencyconverter.exchangerates.application.service.ExchangeRateService;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.ArgumentNotValidError;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.GeneralError;
import com.abelpalomino.currencyconverter.shared.infrastructure.web.constant.StatusCode;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exchange-rates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @ApiResponse(responseCode = StatusCode.OK, description = "List of exchange rates")
    @GetMapping
    public Mono<ResponseEntity<List<ExchangeRateMediumDto>>> findAll() {
        return exchangeRateService.findAll()
                .collectList()
                .map(ResponseEntity::ok);
    }


    @ApiResponse(responseCode = StatusCode.OK, description = "Exchange rate by id")
    @ApiResponse(
            responseCode = StatusCode.NOT_FOUND,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneralError.class)
            )
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ExchangeRateDto>> findById(@PathVariable("id") Long id) {
        return exchangeRateService.findById(id)
                .map(ResponseEntity::ok);
    }


    @ApiResponse(responseCode = StatusCode.CREATED, description = "Exchange rate created")
    @ApiResponse(
            responseCode = StatusCode.BAD_REQUEST,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArgumentNotValidError.class)
            )
    )
    @ApiResponse(
            responseCode = StatusCode.NOT_FOUND,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneralError.class)
            )
    )
    @PostMapping
    public Mono<ResponseEntity<ExchangeRateDto>> create(@Valid @RequestBody ExchangeRateBodyDto exchangeRateBody) {
        return exchangeRateService.create(exchangeRateBody)
                .map(exchangeRate -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(exchangeRate)
                );

    }


    @ApiResponse(responseCode = StatusCode.OK, description = "Exchange rate updated")
    @ApiResponse(
            responseCode = StatusCode.BAD_REQUEST,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArgumentNotValidError.class)
            )
    )
    @ApiResponse(
            responseCode = StatusCode.NOT_FOUND,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneralError.class)
            )
    )
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ExchangeRateDto>> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ExchangeRateBodyDto exchangeRateBody) {
        return exchangeRateService.update(id, exchangeRateBody)
                .map(ResponseEntity::ok);
    }


}
