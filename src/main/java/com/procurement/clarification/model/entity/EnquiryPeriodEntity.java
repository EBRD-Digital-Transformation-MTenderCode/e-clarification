package com.procurement.clarification.model.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("enquiry_period")
@Getter
@Setter
public class EnquiryPeriodEntity {
    @PrimaryKeyColumn(name = "cp_id", type = PrimaryKeyType.PARTITIONED)
    private String cpId;

    @PrimaryKeyColumn(name = "owner", type = PrimaryKeyType.PARTITIONED)
    private String owner;

    @Column(value = "start_date")
    private LocalDateTime startDate;

    @Column(value = "end_date")
    private LocalDateTime endDate;

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EnquiryPeriodEntity)) {
            return false;
        }
        final EnquiryPeriodEntity rhs = (EnquiryPeriodEntity) other;
        return new EqualsBuilder().append(cpId, rhs.cpId)
                                  .append(owner, rhs.owner)
                                  .append(startDate, rhs.startDate)
                                  .append(endDate, rhs.endDate)
                                  .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(cpId)
                                    .append(owner)
                                    .append(startDate)
                                    .append(endDate)
                                    .toHashCode();
    }
}

