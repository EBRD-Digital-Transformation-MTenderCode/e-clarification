package com.procurement.clarification.controller;

import com.procurement.clarification.model.dto.CreateEnquiryDto;
import com.procurement.clarification.model.dto.CreateEnquiryParams;
import com.procurement.clarification.model.dto.UpdateEnquiryDto;
import com.procurement.clarification.model.dto.UpdateEnquiryParams;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.service.EnquiryService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/enquiry")
public class EnquiryController {

    private EnquiryService enquiryService;

    public EnquiryController(final EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createEnquiry(@RequestParam(value = "cpid") final String cpid,
                                                     @RequestParam(value = "stage") final String stage,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam(value = "date") final LocalDateTime date,
                                                     @RequestParam(value = "owner") final String owner,
                                                     @Valid @RequestBody final CreateEnquiryDto dataDto) {
        CreateEnquiryParams params = new CreateEnquiryParams(cpid, stage, date, owner, dataDto);
        return new ResponseEntity<>(enquiryService.saveEnquiry(params), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> updateEnquiry(@RequestParam(value = "cpid") final String cpid,
                                                     @RequestParam(value = "token") final String token,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam(value = "date") final LocalDateTime date,
                                                     @RequestParam(value = "owner") final String owner,
                                                     @Valid @RequestBody final UpdateEnquiryDto dataDto) {
        UpdateEnquiryParams params = new UpdateEnquiryParams(token, cpid, date, owner, dataDto);
        return new ResponseEntity<>(enquiryService.updateEnquiry(params), HttpStatus.OK);
    }

    @GetMapping("/checkEnquiries")
    public ResponseEntity<ResponseDto> checkEnquiries(@RequestParam(value = "cpid") final String cpid,
                                                      @RequestParam(value = "stage") final String stage) {
        return new ResponseEntity<>(enquiryService.checkEnquiries(cpid, stage), HttpStatus.OK);
    }
}
