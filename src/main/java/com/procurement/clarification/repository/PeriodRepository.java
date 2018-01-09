package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.PeriodEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRepository extends CassandraRepository<PeriodEntity, String> {

    @Query(value = "select * from enquiry_period where oc_id=?0 LIMIT 1")
    PeriodEntity getByOcId(String ocId);
}
