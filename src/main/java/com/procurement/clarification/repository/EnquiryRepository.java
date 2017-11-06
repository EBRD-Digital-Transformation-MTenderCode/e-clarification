package com.procurement.clarification.repository;

import com.procurement.clarification.model.entity.EnquiryEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepository extends CassandraRepository<EnquiryEntity,String>{
}
