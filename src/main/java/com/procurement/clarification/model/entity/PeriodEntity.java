package com.procurement.clarification.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("clarification_period")
@Getter
@Setter
public class PeriodEntity {
    @PrimaryKeyColumn(name = "cp_id", type = PrimaryKeyType.PARTITIONED)
    private String cpId;

    @PrimaryKeyColumn(name = "stage", type = PrimaryKeyType.CLUSTERED)
    private String stage;

    @PrimaryKeyColumn(name = "owner")
    private String owner;

    @Column(value = "start_date")
    private Date startDate;

    @Column(value = "enquiry_end_date")
    private Date enquiryEndDate;

    @Column(value = "tender_end_date")
    private Date tenderEndDate;

    public LocalDateTime getStartDate() {
        return LocalDateTime.ofInstant(startDate.toInstant(), ZoneOffset.UTC);
    }

    public LocalDateTime getEnquiryEndDate() {
        return LocalDateTime.ofInstant(enquiryEndDate.toInstant(), ZoneOffset.UTC);
    }

    public LocalDateTime getTenderEndDate() {
        return LocalDateTime.ofInstant(tenderEndDate.toInstant(), ZoneOffset.UTC);
    }
}
