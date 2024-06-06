package com.abelpalomino.currencyconverter.shared.application.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentNotValidError {
    private String message;
    private Map<String, String> error;
}
