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
    public ResponseEntity<ResponseDto> createEnquiry(@RequestParam final String cpId,
                                                     @RequestParam final String stage,
                                                     @RequestParam final String owner,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam final LocalDateTime date,
                                                     @Valid @RequestBody final CreateEnquiryDto dataDto) {
        CreateEnquiryParams params = new CreateEnquiryParams(cpId, stage, date, owner, dataDto);
        return new ResponseEntity<>(enquiryService.saveEnquiry(params), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateEnquiry(@RequestParam final String cpId,
                                                     @RequestParam final String stage,
                                                     @RequestParam final String token,
                                                     @RequestParam final String owner,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @RequestParam final LocalDateTime date,
                                                     @Valid @RequestBody final UpdateEnquiryDto dataDto) {
        UpdateEnquiryParams params = new UpdateEnquiryParams(cpId, stage, token, date, owner, dataDto);
        return new ResponseEntity<>(enquiryService.updateEnquiry(params), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> checkEnquiries(@RequestParam final String cpId,
                                                      @RequestParam final String stage) {
        return new ResponseEntity<>(enquiryService.checkEnquiries(cpId, stage), HttpStatus.OK);
    }
}
