package com.procurement.clarification.exception;

public enum ErrorType {

    DATA_NOT_FOUND("00.01", "Enquiry not found."),
    INVALID_OWNER("00.02", "Invalid owner."),
    INVALID_ID("00.03", "Invalid enquiry id."),
    INVALID_PERIOD("01.02", "Invalid period."),
    INVALID_DATE("01.03", "Date does not match the period."),
    ALREADY_HAS_ANSWER("02.01", "The enquiry already has an answer."),
    OFFSET_RULES_NOT_FOUND("03.01", "Offset rules not found");

    private final String code;
    private final String message;

    ErrorType(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
