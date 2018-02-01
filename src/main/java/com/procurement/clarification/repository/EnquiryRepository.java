package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.EnquiryEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepository extends CassandraRepository<EnquiryEntity, String> {
    @Query(value = "select * from clarification_enquiry where cp_id=?0 and stage=?1 and token_entity=?2 LIMIT 1")
    EnquiryEntity getByCpIdAndStageAndToken(String tenderId, String stage, String token);

    @Query(value = "select COUNT(*) from clarification_enquiry where cp_id=?0 stage=?1 and is_answered=false " +
            "ALLOW FILTERING")
    long getCountByCpIdAndStageAndIsAnswered(String cpId, String stage);
}
