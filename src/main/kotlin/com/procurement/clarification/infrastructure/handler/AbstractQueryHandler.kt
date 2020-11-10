package com.procurement.clarification.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.infrastructure.handler.model.ApiResponseV2
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.model.dto.bpe.getId
import com.procurement.clarification.model.dto.bpe.getVersion
import com.procurement.clarification.utils.toJson

abstract class AbstractQueryHandler<ACTION : Action, R : Any>
    (
    private val logger: Logger
) : AbstractHandler<ACTION, ApiResponseV2>(logger = logger) {

    override fun handle(node: JsonNode): ApiResponseV2 {
        val id = node.getId().get
        val version = node.getVersion().get
        val result = execute(node)
        return when (result) {
            is Result.Success -> {
                if (logger.isDebugEnabled)
                    logger.debug("${action.key} has been executed. Result: ${toJson(result.get)}")
                return ApiResponseV2.Success(version = version, id = id, result = result.get)
            }
            is Result.Failure -> responseError(fail = result.error, version = version, id = id)
        }
    }

    abstract fun execute(node: JsonNode): Result<R, Fail>
}