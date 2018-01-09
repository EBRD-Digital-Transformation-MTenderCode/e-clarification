package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.EnquiryEntity;
import java.util.Optional;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepository extends CassandraRepository<EnquiryEntity, String>{

    @Query(value = "select * from clarification_enquiry where oc_id=?0 and is_answered=false limit 1")
    Optional<EnquiryEntity> getByOcIdNotAnswered(String ocId);
}
