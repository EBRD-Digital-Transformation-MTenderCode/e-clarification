package com.procurement.clarification.model.dto.period.create.request

import com.procurement.clarification.infrastructure.model.dto.period.create.request.CreatePeriodRequest
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class CreatePeriodRequestTest : AbstractDTOTestBase<CreatePeriodRequest>(CreatePeriodRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/period/create/request/request_create_period_full.json")
    }

}
