package com.abelpalomino.currencyconverter.exchangerates.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    private Long id;
    private String originCurrency;
    private BigDecimal originAmount;
    private BigDecimal exchangeRateAmount;
    private String destinationCurrency;
    private BigDecimal destinationAmount;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
