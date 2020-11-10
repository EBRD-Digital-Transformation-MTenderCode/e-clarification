package com.procurement.clarification.application.repository.enquiry

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.token.Token
import com.procurement.clarification.application.repository.enquiry.model.EnquiryEntity
import com.procurement.clarification.lib.functional.Result

interface EnquiryRepository {
    fun findBy(cpid: Cpid, ocid: Ocid): Result<List<EnquiryEntity>, Fail.Incident.Database>
    fun findBy(cpid: Cpid, ocid: Ocid, token: Token): Result<EnquiryEntity?, Fail.Incident.Database>
    fun save(entity: EnquiryEntity): Result<Boolean, Fail.Incident.Database>
}
