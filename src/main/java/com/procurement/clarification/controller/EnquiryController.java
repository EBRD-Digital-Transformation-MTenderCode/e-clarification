package com.procurement.clarification.controller;

import com.procurement.clarification.model.dto.CreateEnquiryDto;
import com.procurement.clarification.model.dto.UpdateEnquiryDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.model.dto.params.CreateEnquiryParams;
import com.procurement.clarification.model.dto.params.UpdateEnquiryParams;
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

    @PostMapping
    public ResponseEntity<ResponseDto> createEnquiry(@RequestParam("identifier") final String cpId,
                                                     @RequestParam("stage") final String stage,
                                                     @RequestParam("owner") final String owner,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam("date") final LocalDateTime dateTime,
                                                     @Valid @RequestBody final CreateEnquiryDto data) {
        final CreateEnquiryParams params = new CreateEnquiryParams(cpId, stage, dateTime, owner, data);
        return new ResponseEntity<>(enquiryService.createEnquiry(params), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> createAnswer(@RequestParam("identifier") final String cpId,
                                                     @RequestParam("stage") final String stage,
                                                     @RequestParam ("token")final String token,
                                                     @RequestParam("owner") final String owner,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam("date") final LocalDateTime dateTime,
                                                     @Valid @RequestBody final UpdateEnquiryDto data) {
        final UpdateEnquiryParams params = new UpdateEnquiryParams(cpId, stage, token, dateTime, owner, data);
        return new ResponseEntity<>(enquiryService.createAnswer(params), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> checkEnquiries(@RequestParam("identifier") final String cpId,
                                                      @RequestParam("stage") final String stage) {
        return new ResponseEntity<>(enquiryService.checkEnquiries(cpId, stage), HttpStatus.OK);
    }
}
