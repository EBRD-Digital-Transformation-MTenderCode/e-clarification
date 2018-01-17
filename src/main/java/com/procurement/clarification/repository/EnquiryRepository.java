package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.EnquiryEntity;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepository extends CassandraRepository<EnquiryEntity, String> {
    @Query(value = "select * from enquiry where cp_id=?0 and enquiry_id=?1 LIMIT 1")
    EnquiryEntity getByCpIdaAndEnquiryId(String tenderId, UUID enquiryId);
}
