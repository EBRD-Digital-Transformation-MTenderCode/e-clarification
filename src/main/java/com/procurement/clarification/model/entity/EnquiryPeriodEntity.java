package com.procurement.clarification.model.entity;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("enquiry_period")
@Getter
@Setter
public class EnquiryPeriodEntity {
    @PrimaryKeyColumn(name = "tender_id", type = PrimaryKeyType.PARTITIONED)
    private String tenderId;

    @Column(value = "start_date")
    private LocalDateTime startDate;

    @Column(value = "end_date")
    private LocalDateTime endDate;
}