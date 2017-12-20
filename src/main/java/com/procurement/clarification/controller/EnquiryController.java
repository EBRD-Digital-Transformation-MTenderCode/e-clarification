package com.procurement.clarification.controller;

import com.procurement.clarification.exception.ValidationException;
import com.procurement.clarification.model.dto.DataDto;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.service.EnquiryPeriodService;
import com.procurement.clarification.service.EnquiryService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enquiry")
public class EnquiryController {

    private EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping(value = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveEnquiry(@Valid @RequestBody final DataDto dataDto,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        enquiryService.insertData(dataDto);
    }

    @PostMapping(value = "/checkEnquiries")
    public ResponseEntity<Boolean> checkEnquiries(@RequestParam final String  ocid) {
        return new ResponseEntity<>(enquiryService.checkEnquiries(ocid), HttpStatus.OK);
    }
}
