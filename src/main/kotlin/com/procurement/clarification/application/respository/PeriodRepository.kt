package com.procurement.clarification.application.respository

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.lib.functional.MaybeFail
import com.procurement.clarification.model.entity.PeriodEntity

interface PeriodRepository {

    fun save(period: PeriodEntity): MaybeFail<Fail.Incident>
}
