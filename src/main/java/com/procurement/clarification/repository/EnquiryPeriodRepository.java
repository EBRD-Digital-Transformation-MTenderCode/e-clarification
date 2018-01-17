package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryPeriodRepository extends CassandraRepository<EnquiryPeriodEntity, String> {

    @Query(value = "select * from enquiry_period where cp_id=?0 LIMIT 1")
    EnquiryPeriodEntity getByCpId(String cpId);

    @Query(value = "select * from enquiry_period where cp_id=?0 and stage=?1 LIMIT 1")
    EnquiryPeriodEntity getByCpIdAndStage(String cpId, String stage);
}
