package com.abelpalomino.currencyconverter.shared.application.dto.exception;


public class UniqueConstraintException extends Exception {

    public UniqueConstraintException(String message) {
        super(message);
    }
}
