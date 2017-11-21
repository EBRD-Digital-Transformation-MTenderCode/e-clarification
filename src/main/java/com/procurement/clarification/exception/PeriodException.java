package com.procurement.clarification.exception;

import lombok.Getter;

@Getter
public class PeriodException extends RuntimeException {

    private String message;

    public PeriodException(String message) {
        this.message = message;
    }
}
