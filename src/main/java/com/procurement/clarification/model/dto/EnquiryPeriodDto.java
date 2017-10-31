package com.procurement.clarification.model.dto;

import java.util.Date;
import lombok.Data;

@Data
public class EnquiryPeriodDto {
    private String tenderId;
    private Date startDate;
    private Date endDate;
}
