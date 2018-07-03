package com.procurement.clarification.controller

import com.procurement.clarification.exception.EnumException
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.model.dto.bpe.ResponseDetailsDto
import com.procurement.clarification.model.dto.bpe.ResponseDto
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(OK)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception) = ResponseDto(false, getErrors("Exception", ex.message), null)

    @ResponseBody
    @ResponseStatus(OK)
    @ExceptionHandler(ErrorException::class)
    fun error(e: ErrorException) = ResponseDto(false, getErrors(e.code, e.msg), null)

    @ResponseBody
    @ResponseStatus(OK)
    @ExceptionHandler(EnumException::class)
    fun enum(e: EnumException) = ResponseDto(false, getErrors(e.code, e.msg), null)

    private fun getErrors(code: String, error: String?) =
            listOf(ResponseDetailsDto(code = "400.05.$code", message = error!!))
}
