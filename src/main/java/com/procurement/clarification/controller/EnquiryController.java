package com.procurement.clarification.controller;

import com.procurement.clarification.exception.ValidationException;
import com.procurement.clarification.model.dto.CreateAnswerRQ;
import com.procurement.clarification.model.dto.CreateAnswerRQDto;
import com.procurement.clarification.model.dto.UpdateAnswerRQ;
import com.procurement.clarification.model.dto.UpdateAnswerRQDto;
import com.procurement.clarification.model.dto.bpe.ResponseDto;
import com.procurement.clarification.service.EnquiryService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enquiry")
public class EnquiryController {

    private EnquiryService enquiryService;

    public EnquiryController(final EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping("/{cpid}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> saveEnquiry(@Valid @RequestBody final CreateAnswerRQDto dataDto,
                                                   @PathVariable(value = "cpid") final String cpid,
                                                   @RequestParam(value = "stage") final String stage,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                   @RequestParam(value = "date") final LocalDateTime date,
                                                   @RequestParam(value = "idPlatform") final String idPlatform,
                                                   final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CreateAnswerRQ answerRQ = new CreateAnswerRQ(cpid, stage, date, idPlatform, dataDto);

        ResponseDto responseDto = enquiryService.saveEnquiry(answerRQ);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/{cpid}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> updateEnquiry(@Valid @RequestBody final UpdateAnswerRQDto dataDto,
                                                   @PathVariable(value = "cpid") final String cpid,
                                                     @RequestParam(value = "token") final String token,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                   @RequestParam(value = "date") final LocalDateTime date,
                                                   @RequestParam(value = "idPlatform") final String idPlatform,
                                                   final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        UpdateAnswerRQ answerRQ = new UpdateAnswerRQ(dataDto, token, cpid,date, idPlatform);

        ResponseDto responseDto = enquiryService.updateEnquiry(answerRQ);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }




}
