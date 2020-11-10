package com.procurement.clarification.infrastructure.handler.v2

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.EnquiryService
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.infrastructure.handler.v2.base.AbstractQueryHandlerV2
import com.procurement.clarification.infrastructure.api.v2.CommandTypeV2
import com.procurement.clarification.infrastructure.api.v2.tryGetParams
import com.procurement.clarification.infrastructure.api.v2.tryParamsToObject
import com.procurement.clarification.infrastructure.handler.v2.model.request.FindEnquiriesRequest
import com.procurement.clarification.infrastructure.handler.v2.model.response.FindEnquiriesResult
import com.procurement.clarification.infrastructure.handler.enquiry.find.convert
import org.springframework.stereotype.Component

@Component
class FindEnquiriesHandler(
    logger: Logger,
    private val enquiryService: EnquiryService
) : AbstractQueryHandlerV2<CommandTypeV2, List<FindEnquiriesResult>>(logger = logger) {

    override fun execute(node: JsonNode): Result<List<FindEnquiriesResult>, Fail> {

        val params = node.tryGetParams()
            .onFailure { return it }
            .tryParamsToObject(FindEnquiriesRequest::class.java)
            .onFailure { return it }
            .convert()
            .onFailure { return it }

        return enquiryService.findEnquiries(params = params)
    }

    override val action: CommandTypeV2
        get() = CommandTypeV2.FIND_ENQUIRIES
}
