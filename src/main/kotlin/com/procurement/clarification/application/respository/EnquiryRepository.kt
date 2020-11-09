package com.procurement.clarification.application.respository

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.enums.Stage
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.model.entity.EnquiryEntity

interface EnquiryRepository {

    fun findAllByCpidAndStage(cpid: Cpid, stage: Stage): Result<List<EnquiryEntity>, Fail.Incident.Database>

}
