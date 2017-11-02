package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.deserialization.JsonDateDeserializer;
import com.procurement.clarification.databind.serialization.JsonDateSerializer;
import java.util.Date;
import lombok.Data;

@Data
public class EnquiryPeriodDto {
    @JsonProperty("tenderId")
    private final String tenderId;
    @JsonProperty("startDate")
    @JsonSerialize(using=JsonDateSerializer.class)
    private final Date startDate;
    @JsonProperty("endDate")
    @JsonSerialize(using=JsonDateSerializer.class)
    private final Date endDate;

    @JsonCreator
    public EnquiryPeriodDto(@JsonProperty("tenderId") final String tenderId,
                            @JsonDeserialize(using=JsonDateDeserializer.class)
                            @JsonProperty("startDate") final Date startDate,
                            @JsonDeserialize(using=JsonDateDeserializer.class)
                            @JsonProperty("endDate") final Date endDate) {
        this.tenderId = tenderId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
