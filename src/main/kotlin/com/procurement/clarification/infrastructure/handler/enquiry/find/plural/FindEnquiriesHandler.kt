package com.procurement.clarification.infrastructure.handler.enquiry.find.plural

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
class FindEnquiriesHandler(
    logger: Logger,
    private val enquiryService: EnquiryService
) : AbstractQueryHandler<Command2Type, List<FindEnquiriesResult>>(logger = logger) {

    override fun execute(node: JsonNode): Result<List<FindEnquiriesResult>, Fail> {

        val params = node.tryGetParams()
            .orForwardFail { fail -> return fail }
            .tryParamsToObject(FindEnquiriesRequest::class.java)
            .orForwardFail { fail -> return fail }
            .convert()
            .orForwardFail { fail -> return fail }

        return enquiryService.findEnquiries(params = params)
    }

    override val action: Command2Type
        get() = Command2Type.FIND_ENQUIRIES
}
