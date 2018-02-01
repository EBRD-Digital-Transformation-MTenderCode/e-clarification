package com.procurement.clarification.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("clarification_enquiry")
@Getter
@Setter
public class EnquiryEntity {
    @PrimaryKeyColumn(name = "cp_id", type = PrimaryKeyType.PARTITIONED)
    private String cpId;

    @PrimaryKeyColumn(name = "stage", type = PrimaryKeyType.CLUSTERED)
    private String stage;

    @PrimaryKeyColumn(value = "token_entity", type = PrimaryKeyType.CLUSTERED)
    private String token;

    @PrimaryKeyColumn(name = "owner")
    private String owner;

    @Column(value = "json_data")
    private String jsonData;

    @Column(value = "is_answered")
    private Boolean isAnswered;
}
