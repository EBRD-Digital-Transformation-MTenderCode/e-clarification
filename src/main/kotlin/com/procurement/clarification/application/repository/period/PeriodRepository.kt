package com.procurement.clarification.application.repository.period

import com.procurement.clarification.application.repository.period.model.PeriodEntity
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.lib.functional.MaybeFail
import com.procurement.clarification.lib.functional.Result

interface PeriodRepository {
    fun find(cpid: Cpid, ocid: Ocid): Result<PeriodEntity?, Fail.Incident.Database.Interaction>
    fun save(period: PeriodEntity): MaybeFail<Fail.Incident>
}
