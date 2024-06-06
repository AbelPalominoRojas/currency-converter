package com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateBodyDto {
    @NotBlank(message = "El campo originCurrency es requerido")
    @Size(min = 3, max = 3, message = "El campo originCurrency debe tener 3 caracteres")
    private String originCurrency;

    @NotNull(message = "El campo originAmount es requerido")
    @Positive(message = "El campo originAmount debe ser mayor a 0")
    private BigDecimal originAmount;

    @NotNull(message = "El campo exchangeRateAmount es requerido")
    @Positive(message = "El campo exchangeRateAmount debe ser mayor a 0")
    private BigDecimal exchangeRateAmount;

    @NotBlank(message = "El campo destinationCurrency es requerido")
    @Size(min = 3, max = 3, message = "El campo destinationCurrency debe tener 3 caracteres")
    private String destinationCurrency;
}
