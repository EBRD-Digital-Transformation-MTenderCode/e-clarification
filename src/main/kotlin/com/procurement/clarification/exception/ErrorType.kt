package com.procurement.clarification.exception

enum class ErrorType constructor(val code: String, val message: String) {
    DATA_NOT_FOUND("00.01", "Enquiry not found."),
    INVALID_OWNER("00.02", "Invalid owner."),
    INVALID_ID("00.03", "Invalid enquiry id."),
    INVALID_PERIOD("01.02", "Invalid period."),
    INVALID_DATE("01.03", "Date does not match the period."),
    ALREADY_HAS_ANSWER("02.01", "The enquiry already has an answer."),
    OFFSET_RULES_NOT_FOUND("03.01", "Offset rules not found");
}
