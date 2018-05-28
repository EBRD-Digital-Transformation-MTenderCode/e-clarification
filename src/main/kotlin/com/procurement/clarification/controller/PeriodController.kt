package com.procurement.clarification.controller

import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.params.PeriodParams
import com.procurement.clarification.service.PeriodService
import java.time.LocalDateTime
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = arrayOf("/period"))
class PeriodController(private val periodService: PeriodService) {

    @PostMapping("/save")
    fun savePeriod(@RequestParam("identifier") cpId: String,
                   @RequestParam("country") country: String,
                   @RequestParam("stage") stage: String,
                   @RequestParam("owner") owner: String,
                   @RequestParam("pmd") pmd: String,
                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                   @RequestParam("startDate")
                   startDate: LocalDateTime,
                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                   @RequestParam("endDate")
                   endDate: LocalDateTime): ResponseEntity<ResponseDto<*>> {
        val params = PeriodParams(cpId, stage, owner, country, pmd, startDate, endDate)
        return ResponseEntity(periodService.calculateAndSavePeriod(params), HttpStatus.CREATED)
    }
}
