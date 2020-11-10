package com.procurement.clarification.infrastructure.handler.v2

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.EnquiryService
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.enquiry.EnquiryId
import com.procurement.clarification.infrastructure.api.v2.CommandTypeV2
import com.procurement.clarification.infrastructure.api.v2.converter.convert
import com.procurement.clarification.infrastructure.api.v2.tryGetParams
import com.procurement.clarification.infrastructure.api.v2.tryParamsToObject
import com.procurement.clarification.infrastructure.handler.v2.base.AbstractQueryHandlerV2
import com.procurement.clarification.infrastructure.handler.v2.model.request.FindEnquiryIdsRequest
import com.procurement.clarification.lib.functional.Result
import org.springframework.stereotype.Component

@Component
class FindEnquiryIdsHandler(
    logger: Logger,
    private val enquiryService: EnquiryService
) : AbstractQueryHandlerV2<CommandTypeV2, List<EnquiryId>>(logger = logger) {

    override fun execute(node: JsonNode): Result<List<EnquiryId>, Fail> {

        val params = node.tryGetParams()
            .onFailure { return it }
            .tryParamsToObject(FindEnquiryIdsRequest::class.java)
            .onFailure { return it }
            .convert()
            .onFailure { return it }

        return enquiryService.findEnquiryIds(params = params)
    }

    override val action: CommandTypeV2
        get() = CommandTypeV2.FIND_ENQUIRY_IDS
}
