package com.procurement.clarification.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.domain.util.ValidationResult
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.dto.ApiSuccessResponse
import com.procurement.clarification.model.dto.bpe.getId
import com.procurement.clarification.model.dto.bpe.getVersion
import com.procurement.clarification.utils.toJson

abstract class AbstractValidationHandler<ACTION : Action>(
    private val logger: Logger
) : AbstractHandler<ACTION, ApiResponse>(logger = logger) {

    override fun handle(node: JsonNode): ApiResponse {
        val id = node.getId().get
        val version = node.getVersion().get

        return when (val result = execute(node)) {
            is ValidationResult.Ok -> ApiSuccessResponse(version = version, id = id)
                .also {
                    logger.info("'${action.key}' has been executed. Result: '${toJson(it)}'")
                }
            is ValidationResult.Fail -> responseError(id = id, version = version, fail = result.error)
        }
    }

    abstract fun execute(node: JsonNode): ValidationResult<Fail>
}