package com.procurement.clarification.model.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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

    @PrimaryKeyColumn(value = "enquiryId", type = PrimaryKeyType.CLUSTERED)
    private UUID enquiryId;

    @PrimaryKeyColumn(name = "owner")
    private String owner;

    @Column(value = "json_data")
    private String jsonData;

    @Column(value = "is_answered")
    private Boolean isAnswered;

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EnquiryEntity)) {
            return false;
        }
        final EnquiryEntity rhs = (EnquiryEntity) other;
        return new EqualsBuilder().append(cpId, rhs.cpId)
                                  .append(enquiryId, rhs.enquiryId)
                                  .append(owner, rhs.owner)
                                  .append(jsonData, rhs.jsonData)
                                  .append(isAnswered, rhs.isAnswered)
                                  .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(cpId)
                                    .append(enquiryId)
                                    .append(owner)
                                    .append(jsonData)
                                    .append(isAnswered)
                                    .toHashCode();
    }
}
