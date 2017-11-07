package com.procurement.clarification.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("enquiry_rules")
@Getter
@Setter
public class RulesEntity {
    @PrimaryKeyColumn(name = "iso", type = PrimaryKeyType.PARTITIONED)
    private String iso;

    @Column(value = "offset")
    private int offset;
}
