package com.procurement.clarification.controller;

import com.procurement.clarification.exception.ValidationException;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.PeriodDataDto;
import com.procurement.clarification.service.EnquiryPeriodService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/period")
public class PeriodController {

    private EnquiryPeriodService enquiryPeriodService;

    public PeriodController(final EnquiryPeriodService enquiryPeriodService) {
        this.enquiryPeriodService = enquiryPeriodService;
    }

    @PostMapping("/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void savePeriod(@Valid @RequestBody final EnquiryPeriodDto dataDto,
                                  final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        enquiryPeriodService.saveEnquiryPeriod(dataDto);
    }

    @PostMapping("/calculateAndSave")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void calculateAndSavePeriod(@Valid @RequestBody final PeriodDataDto dataDto,
                                              final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        enquiryPeriodService.calculateAndSaveEnquiryPeriod(dataDto);
    }
}
