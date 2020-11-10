package com.procurement.clarification.infrastructure.api.v1

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.clarification.exception.EnumException
import com.procurement.clarification.exception.ErrorException

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseErrorDto(
    val code: String,
    val description: String?
)

fun getExceptionResponseDto(exception: Exception) = ResponseDto(
    errors = listOf(
        ResponseErrorDto(
            code = "400.05.00",
            description = exception.message
        )
    )
)

fun getErrorExceptionResponseDto(error: ErrorException, id: String? = null) = ResponseDto(
    errors = listOf(
        ResponseErrorDto(
            code = "400.05." + error.code,
            description = error.msg
        )
    ),
    id = id
)

fun getEnumExceptionResponseDto(error: EnumException, id: String? = null) = ResponseDto(
    errors = listOf(
        ResponseErrorDto(
            code = "400.05." + error.code,
            description = error.msg
        )
    ),
    id = id
)
