package com.procurement.clarification.controller

import com.procurement.clarification.exception.EnumException
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.model.dto.bpe.*
import com.procurement.clarification.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val commandService: CommandService) {

    @PostMapping
    fun command(@RequestBody cm: CommandMessage): ResponseEntity<ResponseDto> {
        return ResponseEntity(commandService.execute(cm), HttpStatus.OK)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception): ResponseDto {
        return when (ex) {
            is ErrorException -> getErrorExceptionResponseDto(ex)
            is EnumException -> getEnumExceptionResponseDto(ex)
            else -> getExceptionResponseDto(ex)
        }
    }
}



