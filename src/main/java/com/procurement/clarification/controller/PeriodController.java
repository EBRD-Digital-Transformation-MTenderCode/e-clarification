package com.procurement.clarification.controller;

import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.dto.params.PeriodParams;
import com.procurement.clarification.service.PeriodService;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/period")
public class PeriodController {

    private final PeriodService periodService;

    public PeriodController(final PeriodService periodService) {
        this.periodService = periodService;
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> savePeriod(@RequestParam("identifier") final String cpId,
                                                  @RequestParam("country") final String country,
                                                  @RequestParam("stage") final String stage,
                                                  @RequestParam("owner") final String owner,
                                                  @RequestParam("pmd") final String pmd,
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                  @RequestParam("startDate") final LocalDateTime startDate,
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                  @RequestParam("endDate") final LocalDateTime endDate) {
        final PeriodParams params = new PeriodParams(cpId, stage, owner, country, pmd, startDate, endDate);
        return new ResponseEntity<>(periodService.calculateAndSavePeriod(params), HttpStatus.CREATED);
    }
}
