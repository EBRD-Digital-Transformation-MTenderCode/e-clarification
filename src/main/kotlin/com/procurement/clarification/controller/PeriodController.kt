package com.procurement.clarification.controller

import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.params.PeriodParams
import com.procurement.clarification.service.PeriodService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping(path = ["/period"])
class PeriodController(private val periodService: PeriodService) {

    @PostMapping("/save")
    fun savePeriod(@RequestParam("cpid") cpId: String,
                   @RequestParam("country") country: String,
                   @RequestParam("stage") stage: String,
                   @RequestParam("owner") owner: String,
                   @RequestParam("pmd") pmd: String,
                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                   @RequestParam("startDate") startDate: LocalDateTime,
                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                   @RequestParam("endDate") endDate: LocalDateTime,
                   @RequestParam("setExtendedPeriod") setExtendedPeriod: Boolean): ResponseEntity<ResponseDto> {
        val params = PeriodParams(
                cpId = cpId,
                stage = stage,
                owner = owner,
                country = country,
                pmd = pmd,
                startDate = startDate,
                endDate = endDate,
                setExtendedPeriod = setExtendedPeriod)
        return ResponseEntity(
                periodService.calculateAndSavePeriod(params),
                HttpStatus.CREATED)
    }

    @GetMapping
    fun getPeriod(@RequestParam("cpid") cpId: String,
                  @RequestParam("stage") stage: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                periodService.getPeriod(cpId = cpId, stage = stage),
                HttpStatus.CREATED)
    }
}
