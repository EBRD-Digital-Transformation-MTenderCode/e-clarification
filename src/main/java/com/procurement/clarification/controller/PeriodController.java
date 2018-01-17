package com.procurement.clarification.controller;

import com.procurement.clarification.exception.ValidationException;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.PeriodDataDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.service.EnquiryPeriodService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/enquiryperiod")
public class PeriodController {

    private EnquiryPeriodService enquiryPeriodService;

    public PeriodController(final EnquiryPeriodService enquiryPeriodService) {
        this.enquiryPeriodService = enquiryPeriodService;
    }

    @PostMapping("/{cpid}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> calculateAndSavePeriod(
                                       @PathVariable(value = "cpid") final String cpid,
                                       @RequestParam(value = "country")
                                       final String country,
                                       @RequestParam(value = "pmd")
                                       final String pmd,
                                       @RequestParam(value = "stage")
                                       final String stage,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       @RequestParam(value = "startDate")
                                       final LocalDateTime startDate,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       @RequestParam(value = "endDate")
                                       final LocalDateTime endDate,
                                       @RequestParam(value = "owner")
                                       final String owner) {


        ResponseDto responseDto = enquiryPeriodService.calculateAndSaveEnquiryPeriod(cpid,country,pmd,startDate,endDate,owner);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
}
