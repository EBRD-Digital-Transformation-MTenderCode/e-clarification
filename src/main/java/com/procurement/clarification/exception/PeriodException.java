package com.procurement.clarification.exception;

import lombok.Getter;

@Getter
public class PeriodException extends RuntimeException {

    private final String message;

    public PeriodException(final String message) {
        this.message = message;
    }
}
