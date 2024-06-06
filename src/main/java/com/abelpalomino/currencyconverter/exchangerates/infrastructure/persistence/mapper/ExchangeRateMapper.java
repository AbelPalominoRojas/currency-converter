package com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.mapper;

import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import com.abelpalomino.currencyconverter.exchangerates.infrastructure.persistence.entity.ExchangeRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeRateMapper {
    ExchangeRateEntity toEntity(ExchangeRate domain);

    ExchangeRate toModel(ExchangeRateEntity entity);
}
