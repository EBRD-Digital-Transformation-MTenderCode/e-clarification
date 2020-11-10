package com.procurement.clarification.model.dto.period.create.response

import com.procurement.clarification.infrastructure.handler.v1.model.response.CreatePeriodResponse
import com.procurement.clarification.model.dto.AbstractDTOTestBase
import org.junit.Test

class CreatePeriodResponseTest : AbstractDTOTestBase<CreatePeriodResponse>(CreatePeriodResponse::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/period/create/response/response_create_period_full.json")
    }
}
