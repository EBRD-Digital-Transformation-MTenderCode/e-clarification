package com.procurement.clarification.controller

import com.procurement.clarification.model.dto.CreateEnquiryDto
import com.procurement.clarification.model.dto.UpdateEnquiryDto
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.params.CreateEnquiryParams
import com.procurement.clarification.model.dto.params.UpdateEnquiryParams
import com.procurement.clarification.service.EnquiryService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/enquiry")
class EnquiryController(private val enquiryService: EnquiryService) {

    @PostMapping
    fun createEnquiry(@RequestParam("identifier") cpId: String,
                      @RequestParam("stage") stage: String,
                      @RequestParam("owner") owner: String,
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                      @RequestParam("date") dateTime: LocalDateTime,
                      @Valid @RequestBody data: CreateEnquiryDto): ResponseEntity<ResponseDto<*>> {
        val params = CreateEnquiryParams(
                cpId = cpId,
                stage = stage,
                dateTime = dateTime,
                owner = owner,
                data = data)
        return ResponseEntity(
                enquiryService.createEnquiry(params),
                HttpStatus.CREATED)
    }

    @PutMapping
    fun createAnswer(@RequestParam("identifier") cpId: String,
                     @RequestParam("stage") stage: String,
                     @RequestParam("token") token: String,
                     @RequestParam("owner") owner: String,
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     @RequestParam("date") dateTime: LocalDateTime,
                     @Valid @RequestBody data: UpdateEnquiryDto): ResponseEntity<ResponseDto<*>> {
        val params = UpdateEnquiryParams(
                cpId = cpId,
                stage = stage,
                token = token,
                dateTime = dateTime,
                owner = owner,
                data = data)
        return ResponseEntity(enquiryService.createAnswer(params), HttpStatus.OK)
    }

    @GetMapping
    fun checkEnquiries(@RequestParam("identifier") cpId: String,
                       @RequestParam("stage") stage: String): ResponseEntity<ResponseDto<*>> {
        return ResponseEntity(enquiryService.checkEnquiries(cpId, stage), HttpStatus.OK)
    }
}
