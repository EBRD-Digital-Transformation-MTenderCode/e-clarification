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
    @PrimaryKeyColumn(name = "oc_id", type = PrimaryKeyType.PARTITIONED)
    private String ocId;

    @PrimaryKeyColumn(value = "enquiry_id", type = PrimaryKeyType.CLUSTERED)
    private UUID enquiryId;

    @Column(value = "json_data")
    private String jsonData;

    @Column(value = "is_answered")
    private Boolean isAnswered;

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(getClass() == obj.getClass())) {
            return false;
        } else {
            final EnquiryEntity tmp = (EnquiryEntity) obj;

            return tmp.ocId.equals(this.ocId) &&
                tmp.enquiryId.equals(this.enquiryId) &&
                tmp.jsonData.equals(this.jsonData) &&
                tmp.isAnswered.equals(this.isAnswered);
        }
    }

    @Override
    public int hashCode() {
        return enquiryId.hashCode();
    }
}
