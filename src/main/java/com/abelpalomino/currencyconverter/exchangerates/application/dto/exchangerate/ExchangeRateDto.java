package com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
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
