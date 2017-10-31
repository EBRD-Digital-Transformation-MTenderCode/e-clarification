package com.procurement.clarification.controller;

import com.procurement.clarification.model.dto.DataDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenders")
public class TenderController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataDto addDataDto(@RequestBody final DataDto dataDto){
        return new DataDto(dataDto.getTender());
    }
}
