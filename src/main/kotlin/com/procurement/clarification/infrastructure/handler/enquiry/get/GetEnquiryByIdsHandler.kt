package com.procurement.clarification.infrastructure.handler.enquiry.get

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.EnquiryService
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.infrastructure.handler.AbstractQueryHandler
import com.procurement.clarification.model.dto.bpe.Command2Type
import com.procurement.clarification.model.dto.bpe.tryGetParams
import com.procurement.clarification.model.dto.bpe.tryParamsToObject
import org.springframework.stereotype.Component

@Component
class GetEnquiryByIdsHandler(
    logger: Logger,
    private val enquiryService: EnquiryService
) : AbstractQueryHandler<Command2Type, List<GetEnquiryByIdsResult>>(logger = logger) {

    override fun execute(node: JsonNode): Result<List<GetEnquiryByIdsResult>, Fail> {

        val params = node.tryGetParams()
            .orForwardFail { fail -> return fail }
            .tryParamsToObject(GetEnquiryByIdsRequest::class.java)
            .orForwardFail { fail -> return fail }
            .convert()
            .orForwardFail { fail -> return fail }

        return enquiryService.getEnquiryByIds(params = params)
    }

    override val action: Command2Type
        get() = Command2Type.FIND_ENQUIRY_IDS
}
