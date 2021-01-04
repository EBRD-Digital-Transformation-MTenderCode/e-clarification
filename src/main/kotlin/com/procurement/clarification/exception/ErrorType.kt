package com.procurement.clarification.exception

enum class ErrorType constructor(val code: String, val message: String) {
    INVALID_JSON_TYPE("00.00", "Invalid type: "),
    DATA_NOT_FOUND("00.01", "Enquiry not found."),
    PERIOD_NOT_FOUND("00.02", "Period not found."),
    INVALID_OWNER("00.03", "Invalid owner."),
    INVALID_ID("00.04", "Invalid enquiry id."),
    INVALID_PERIOD("01.02", "Invalid period."),
    INVALID_DATE("01.03", "Date does not match the period."),
    INVALID_ANSWER("01.04", "The answer should not be empty."),
    ALREADY_HAS_ANSWER("02.01", "The enquiry already has an answer."),
    OFFSET_RULES_NOT_FOUND("03.01", "Offset rules not found"),
    INTERVAL_RULES_NOT_FOUND("03.02", "Interval rules not found."),
    CONTEXT("20.01", "Context parameter not found."),
    INVALID_FORMAT_OF_ATTRIBUTE("20.02", "Invalid attribute format."),
    INVALID_FORMAT_TOKEN("20.03", "Invalid format token."),
    SHIFT_PERIOD_CREATE_ENQUIRIES_RULES_NOT_FOUND("20.04", "ShiftPeriodCreateEnquiries rules not found."),
    INCORRECT_VALUE_ATTRIBUTE("20.04", "Incorrect an attribute value.");
}
