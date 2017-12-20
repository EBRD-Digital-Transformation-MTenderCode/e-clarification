package com.procurement.clarification.model.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("clarification_period")
@Getter
@Setter
public class EnquiryPeriodEntity {
    @PrimaryKeyColumn(name = "oc_id", type = PrimaryKeyType.PARTITIONED)
    private String ocId;

    @Column(value = "start_date")
    private LocalDateTime startDate;

    @Column(value = "end_date")
    private LocalDateTime endDate;

    public boolean equals(Object obj) {
        if (obj == this)
            return true;

     /* obj ссылается на null */

        if (obj == null)
            return false;

     /* Удостоверимся, что ссылки имеют тот же самый тип */

        if (!(getClass() == obj.getClass()))
            return false;
        else {
            EnquiryPeriodEntity tmp = (EnquiryPeriodEntity) obj;
            if (tmp.ocId == this.ocId &&
                tmp.endDate == this.endDate &&
                tmp.startDate == this.startDate)
                return true;
            else
                return false;
        }
    }
}
