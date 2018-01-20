package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.EnquiryEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepository extends CassandraRepository<EnquiryEntity, String> {
    @Query(value = "select * from clarification_enquiry where cp_id=?0 and token_entity=?1 LIMIT 1")
    Optional<EnquiryEntity> getByCpIdaAndToken(String tenderId, String token);

    @Query(value = "select COUNT(*) from clarification_enquiry where cp_id=?0 and is_answered=false ALLOW FILTERING")
    long getCountByCpIdAndIsAnswered(String cpId);
}
