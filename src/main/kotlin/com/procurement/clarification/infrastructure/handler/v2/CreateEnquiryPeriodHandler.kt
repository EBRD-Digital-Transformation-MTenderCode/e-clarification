package com.procurement.clarification.infrastructure.handler.v2

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.application.service.Transform
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.infrastructure.handler.v2.base.AbstractHistoricalHandlerV2
import com.procurement.clarification.infrastructure.handler.HistoryRepository
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.flatMap
import com.procurement.clarification.infrastructure.api.v2.CommandTypeV2
import com.procurement.clarification.infrastructure.api.v2.tryGetParams
import com.procurement.clarification.infrastructure.api.v2.tryParamsToObject
import com.procurement.clarification.infrastructure.handler.v2.model.request.CreateEnquiryPeriodRequest
import com.procurement.clarification.infrastructure.handler.v2.model.response.CreateEnquiryPeriodResult
import com.procurement.clarification.infrastructure.handler.v2.converter.convert
import com.procurement.clarification.application.service.PeriodService
import org.springframework.stereotype.Component

@Component
class CreateEnquiryPeriodHandler(
    logger: Logger,
    transform: Transform,
    historyRepository: HistoryRepository,
    private val periodService: PeriodService
) : AbstractHistoricalHandlerV2<CommandTypeV2, CreateEnquiryPeriodResult>(
    logger = logger,
    transform = transform,
    historyRepository = historyRepository,
    target = CreateEnquiryPeriodResult::class.java
) {

    override fun execute(node: JsonNode): Result<CreateEnquiryPeriodResult, Fail> {

        val params = node.tryGetParams()
            .flatMap { it.tryParamsToObject(CreateEnquiryPeriodRequest::class.java) }
            .flatMap { it.convert() }
            .onFailure { return it }

        return periodService.createEnquiryPeriod(params = params)
    }

    override val action: CommandTypeV2
        get() = CommandTypeV2.CREATE_ENQUIRY_PERIOD
}
