package com.procurement.clarification.infrastructure.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.infrastructure.api.ApiVersion
import com.procurement.clarification.infrastructure.handler.v2.FindEnquiriesHandler
import com.procurement.clarification.infrastructure.handler.v2.FindEnquiryIdsHandler
import com.procurement.clarification.infrastructure.handler.v2.CreateEnquiryPeriodHandler
import com.procurement.clarification.infrastructure.api.v2.ApiResponseV2
import com.procurement.clarification.infrastructure.bind.api.CommandId
import com.procurement.clarification.infrastructure.api.v2.CommandTypeV2
import com.procurement.clarification.infrastructure.api.v2.errorResponse
import com.procurement.clarification.infrastructure.api.v2.getAction
import com.procurement.clarification.infrastructure.api.v2.getId
import com.procurement.clarification.infrastructure.api.v2.getVersion
import org.springframework.stereotype.Service

@Service
class CommandServiceV2(
    private val logger: Logger,
    private val findEnquiryIdsHandler: FindEnquiryIdsHandler,
    private val findEnquiriesHandler: FindEnquiriesHandler,
    private val createEnquiryPeriodHandler: CreateEnquiryPeriodHandler
) {

    fun execute(request: JsonNode): ApiResponseV2 {

        val version = request.getVersion()
            .onFailure {
                val id = request.getId().getOrElse(CommandId.NaN)
                return errorResponse(fail = it.reason, version = ApiVersion.NaN, id = id, logger = logger)
            }

        val id = request.getId()
            .onFailure {
                return errorResponse(
                    fail = it.reason,
                    version = version,
                    id = CommandId.NaN,
                    logger = logger
                )
            }

        val action = request.getAction()
            .onFailure { return errorResponse(fail = it.reason, version = version, id = id, logger = logger) }

        val response: ApiResponseV2 = when (action) {
            CommandTypeV2.CREATE_ENQUIRY_PERIOD -> createEnquiryPeriodHandler.handle(node = request)
            CommandTypeV2.FIND_ENQUIRY_IDS -> findEnquiryIdsHandler.handle(node = request)
            CommandTypeV2.FIND_ENQUIRIES -> findEnquiriesHandler.handle(node = request)
        }

        logger.info("DataOfResponse: '$response'.")
        return response
    }
}
