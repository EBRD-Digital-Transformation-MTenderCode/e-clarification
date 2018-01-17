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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class EnquiryController {

    private EnquiryService enquiryService;

    public EnquiryController(final EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping("enquiry/{cpid}")
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

        final CreateAnswerRQ answerRQ = new CreateAnswerRQ(cpid, stage, date, idPlatform, dataDto);

        final ResponseDto responseDto = enquiryService.saveEnquiry(answerRQ);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("enquiry/{cpid}")
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

        final UpdateAnswerRQ answerRQ = new UpdateAnswerRQ(dataDto, token, cpid, date, idPlatform);

        final ResponseDto responseDto = enquiryService.updateEnquiry(answerRQ);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("get/enquiry/{cpid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseDto> chackEnquires(@PathVariable(value = "cpid") final String cpid,
                                                     @RequestParam(value = "stage") final String stage) {

        final ResponseDto responseDto = enquiryService.checkEnquiries(cpid, stage);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
