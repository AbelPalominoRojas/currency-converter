package com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("exchange_rates")
public class ExchangeRateEntity {
    @Id
    private Long id;

    @Column("origin_currency")
    private String originCurrency;

    @Column("origin_amount")
    private BigDecimal originAmount;

    @Column("exchange_rate_amount")
    private BigDecimal exchangeRateAmount;

    @Column("destination_currency")
    private String destinationCurrency;

    @Column("destination_amount")
    private BigDecimal destinationAmount;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
