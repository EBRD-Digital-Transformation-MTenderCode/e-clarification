package com.procurement.clarification.infrastructure.handler.find.enquiries.period

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.EnquiryService
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.bind
import com.procurement.clarification.infrastructure.handler.AbstractQueryHandler
import com.procurement.clarification.model.dto.bpe.Command2Type
import com.procurement.clarification.model.dto.bpe.tryGetParams
import com.procurement.clarification.model.dto.bpe.tryParamsToObject
import org.springframework.stereotype.Component

@Component
class CreateEnquiryPeriodHandler(
    logger: Logger,
    private val enquiryService: EnquiryService
) : AbstractQueryHandler<Command2Type, CreateEnquiryPeriodResult>(logger = logger) {

    override fun execute(node: JsonNode): Result<CreateEnquiryPeriodResult, Fail> {

        val params = node.tryGetParams()
            .bind {  it.tryParamsToObject(CreateEnquiryPeriodRequest::class.java)}
            .bind {  it.convert()}
            .orForwardFail { fail -> return fail }

        return enquiryService.createEnquiryPeriod(params = params)
    }

    override val action: Command2Type
        get() = Command2Type.CREATE_ENQUIRY_PERIOD
}
