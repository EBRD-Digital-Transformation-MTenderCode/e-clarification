package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.deserialization.JsonDateDeserializer;
import com.procurement.clarification.databind.deserialization.JsonLocalDateDeserializer;
import com.procurement.clarification.databind.serialization.JsonDateSerializer;
import com.procurement.clarification.databind.serialization.JsonLocalDateSerializer;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class EnquiryPeriodDto {
    @JsonProperty("tenderId")
    private final String tenderId;
    @JsonProperty("startDate")
    @JsonSerialize(using=JsonLocalDateSerializer.class)
    private final LocalDateTime startDate;
    @JsonProperty("endDate")
    @JsonSerialize(using=JsonLocalDateSerializer.class)
    private final LocalDateTime endDate;

    @JsonCreator
    public EnquiryPeriodDto(@JsonProperty("tenderId") final String tenderId,
                            @JsonDeserialize(using=JsonLocalDateDeserializer.class)
                            @JsonProperty("startDate") final LocalDateTime startDate,
                            @JsonDeserialize(using=JsonLocalDateDeserializer.class)
                            @JsonProperty("endDate") final LocalDateTime endDate) {
        this.tenderId = tenderId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
