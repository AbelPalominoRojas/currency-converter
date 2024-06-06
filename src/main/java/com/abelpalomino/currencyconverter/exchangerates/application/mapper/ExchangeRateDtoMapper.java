package com.abelpalomino.currencyconverter.exchangerates.application.mapper;

import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateBodyDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateDto;
import com.abelpalomino.currencyconverter.exchangerates.application.dto.exchangerate.ExchangeRateMediumDto;
import com.abelpalomino.currencyconverter.exchangerates.domain.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeRateDtoMapper {

    ExchangeRateDto toDto(ExchangeRate exchangeRate);

    ExchangeRateMediumDto toMediumDto(ExchangeRate exchangeRate);

    ExchangeRate toEntity(ExchangeRateBodyDto exchangeRateBody);

    void updateEntity(@MappingTarget ExchangeRate exchangeRate, ExchangeRateBodyDto exchangeRateBody);

}
