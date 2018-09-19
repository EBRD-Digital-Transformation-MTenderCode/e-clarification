package com.procurement.clarification.controller

import com.procurement.clarification.exception.EnumException
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.model.dto.bpe.*
import com.procurement.clarification.service.EnquiryService
import com.procurement.clarification.service.PeriodService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val enquiryService: EnquiryService,
                        private val periodService: PeriodService) {

    @PostMapping
    fun command(@RequestBody commandMessage: CommandMessage): ResponseEntity<ResponseDto> {
        return ResponseEntity(execute(commandMessage), HttpStatus.OK)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.CREATE_ENQUIRY -> enquiryService.createEnquiry(cm)
            CommandType.CREATE_ANSWER -> enquiryService.createAnswer(cm)
            CommandType.CHECK_ENQUIRIES -> enquiryService.checkEnquiries(cm)
            CommandType.GET_PERIOD -> periodService.getPeriod(cm)
            CommandType.CALCULATE_SAVE_PERIOD -> periodService.calculateAndSavePeriod(cm)
        }
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



