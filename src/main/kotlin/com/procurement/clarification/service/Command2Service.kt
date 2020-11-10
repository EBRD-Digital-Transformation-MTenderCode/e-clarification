package com.procurement.clarification.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.handler.enquiry.find.FindEnquiriesHandler
import com.procurement.clarification.infrastructure.handler.enquiry.id.find.FindEnquiryIdsHandler
import com.procurement.clarification.infrastructure.handler.enquiry.period.create.CreateEnquiryPeriodHandler
import com.procurement.clarification.infrastructure.model.CommandId
import com.procurement.clarification.model.dto.bpe.Command2Type
import com.procurement.clarification.model.dto.bpe.errorResponse
import com.procurement.clarification.model.dto.bpe.getAction
import com.procurement.clarification.model.dto.bpe.getId
import com.procurement.clarification.model.dto.bpe.getVersion
import org.springframework.stereotype.Service

@Service
class Command2Service(
    private val logger: Logger,
    private val findEnquiryIdsHandler: FindEnquiryIdsHandler,
    private val findEnquiriesHandler: FindEnquiriesHandler,
    private val createEnquiryPeriodHandler: CreateEnquiryPeriodHandler
) {

    fun execute(request: JsonNode): ApiResponse {

        val version = request.getVersion()
            .onFailure {
                val id = request.getId().getOrElse(CommandId.NaN)
                return errorResponse(fail = it.reason, id = id)
            }

        val id = request.getId()
            .onFailure { return errorResponse(fail = it.reason, version = version, id = CommandId.NaN) }

        val action = request.getAction()
            .onFailure { return errorResponse(fail = it.reason, version = version, id = id) }

        val response: ApiResponse = when (action) {
            Command2Type.CREATE_ENQUIRY_PERIOD -> createEnquiryPeriodHandler.handle(node = request)
            Command2Type.FIND_ENQUIRY_IDS -> findEnquiryIdsHandler.handle(node = request)
            Command2Type.FIND_ENQUIRIES -> findEnquiriesHandler.handle(node = request)
        }

        logger.info("DataOfResponse: '$response'.")
        return response
    }
}
