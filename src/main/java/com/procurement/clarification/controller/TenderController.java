package com.procurement.clarification.controller;

import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.service.EnquiryPeriodService;
import com.procurement.clarification.service.EnquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenders")
public class TenderController {

    private EnquiryPeriodService enquiryPeriodService;
    private EnquiryService enquiryService;

    public TenderController(EnquiryPeriodService enquiryPeriodService,EnquiryService enquiryService) {
        this.enquiryPeriodService = enquiryPeriodService;
        this.enquiryService = enquiryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataDto addDataDto(@RequestBody final DataDto dataDto){
        return new DataDto(dataDto.getTender());
    }

    @RequestMapping(value = "/enquiryPeriod", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addEnquiryPeriod(@RequestParam("offset") Integer offset, @RequestBody final EnquiryPeriodDto enquiryPeriod){
        enquiryPeriodService.insertData(enquiryPeriod);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/enquire",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addEnquiry(@RequestBody final DataDto enquiry){
        enquiryService.insertData(enquiry);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

}
