package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.PeriodEntity;
import java.util.Optional;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRepository extends CassandraRepository<PeriodEntity, String> {

    @Query(value = "select * from clarification_period where cp_id=?0 LIMIT 1")
    Optional<PeriodEntity> getByCpId(String cpId);

}


