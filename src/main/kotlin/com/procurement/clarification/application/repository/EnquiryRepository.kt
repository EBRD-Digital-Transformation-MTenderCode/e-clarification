package com.procurement.clarification.application.repository

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.enums.Stage
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.model.entity.EnquiryEntity

interface EnquiryRepository {

    fun findAllByCpidAndStage(cpid: Cpid, stage: Stage): Result<List<EnquiryEntity>, Fail.Incident.Database>

}
