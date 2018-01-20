package com.procurement.clarification.model.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("enquiry")
@Getter
@Setter
public class EnquiryEntity {
    @PrimaryKeyColumn(name = "cp_id", type = PrimaryKeyType.PARTITIONED)
    private String cpId;

    @PrimaryKeyColumn(value = "enquiry_id", type = PrimaryKeyType.CLUSTERED)
    private UUID enquiryId;

    @PrimaryKeyColumn(name = "owner")
    private String owner;

    @Column(value = "json_data")
    private String jsonData;

    @Column(value = "is_answered")
    private Boolean isAnswered;
}
