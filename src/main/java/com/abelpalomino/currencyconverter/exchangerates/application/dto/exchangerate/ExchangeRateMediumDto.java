package com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateMediumDto {
    private Long id;
    private String originCurrency;
    private BigDecimal originAmount;
    private BigDecimal exchangeRateAmount;
    private String destinationCurrency;
    private BigDecimal destinationAmount;
}
