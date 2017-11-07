package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.clarification.databind.LocalDateTimeDeserializer;
import com.procurement.clarification.databind.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonPropertyOrder({
    "ocid",
    "startDate",
    "endDate"
})
public class EnquiryPeriodDto {
    @NotNull
    @JsonProperty("ocid")
    private String ocId;
    @JsonProperty("startDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime startDate;
    @JsonProperty("endDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    @JsonCreator
    public EnquiryPeriodDto(@JsonProperty("ocid") final String ocId,
                            @JsonProperty("startDate") final LocalDateTime startDate,
                            @JsonProperty("endDate") final LocalDateTime endDate) {
        this.ocId = ocId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
