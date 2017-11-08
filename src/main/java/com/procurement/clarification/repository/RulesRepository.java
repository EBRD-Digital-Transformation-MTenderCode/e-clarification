package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.RulesEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RulesRepository extends CassandraRepository<RulesEntity, String> {
    @Query(value = "select * from enquiry_rules where iso=?0 LIMIT 1")
    RulesEntity getByIso(String iso);
}
