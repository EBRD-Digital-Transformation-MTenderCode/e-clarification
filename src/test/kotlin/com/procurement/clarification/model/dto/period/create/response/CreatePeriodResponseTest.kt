package com.procurement.access.model.dto.pn

import com.procurement.clarification.infrastructure.model.dto.period.create.response.CreatePeriodResponse
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class CreatePeriodResponseTest : AbstractDTOTestBase<CreatePeriodResponse>(CreatePeriodResponse::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/period/create/response/response_create_period_full.json")
    }
}
