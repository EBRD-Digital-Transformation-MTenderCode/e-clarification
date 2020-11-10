package com.procurement.clarification.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.application.service.Transform

import com.procurement.clarification.domain.extension.nowDefaultUTC
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.dto.ApiSuccessResponse
import com.procurement.clarification.infrastructure.dto.ApiVersion
import com.procurement.clarification.infrastructure.model.CommandId
import com.procurement.clarification.infrastructure.repository.history.model.HistoryEntity
import com.procurement.clarification.infrastructure.web.api.ApiResponseGenerator.generateResponseOnFailure

import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.model.dto.bpe.getAction
import com.procurement.clarification.model.dto.bpe.getId
import com.procurement.clarification.model.dto.bpe.getVersion
import com.procurement.clarification.utils.toJson

abstract class AbstractHistoricalHandler<ACTION : Action, R>(
    private val target: Class<R>,
    private val transform: Transform,
    private val historyRepository: HistoryRepository,
    private val logger: Logger
) : Handler<ACTION, ApiResponse> {

    override fun handle(node: JsonNode): ApiResponse {
        val version = node.getVersion()
            .onFailure {
                return generateResponseOnFailure(
                    fail = it.reason,
                    version = ApiVersion.NaN,
                    id = CommandId.NaN,
                    logger = logger
                )
            }
        val id = node.getId()
            .onFailure {
                return generateResponseOnFailure(
                    fail = it.reason,
                    version = version,
                    id = CommandId.NaN,
                    logger = logger
                )
            }

        val action = node.getAction()
            .onFailure {
                return generateResponseOnFailure(fail = it.reason, version = version, id = id, logger = logger)
            }

        val history = historyRepository.getHistory(id, action)
            .onFailure {
                return generateResponseOnFailure(fail = it.reason, version = version, id = id, logger = logger)
            }

        if (history != null) {
            val result = transform.tryDeserialization(value = history, target = target)
                .onFailure {
                    return generateResponseOnFailure(
                        fail = Fail.Incident.Database.Parsing(
                            column = "json_data",
                            value = history,
                            exception = it.reason.exception
                        ),
                        id = id,
                        version = version,
                        logger = logger
                    )
                }
            return ApiSuccessResponse(version = version, id = id, result = result)
        }

        return execute(node)
            .onFailure {
                return generateResponseOnFailure(fail = it.reason, version = version, id = id, logger = logger)
            }
            .let { result ->
                if (result != null) {
                    val historyEntity = HistoryEntity(
                        commandId = id,
                        action = action,
                        date = nowDefaultUTC(),
                        data = toJson(result)
                    )
                    historyRepository.saveHistory(historyEntity)
                }
                if (logger.isDebugEnabled)
                    logger.debug("${action.key} has been executed. Result: '${transform.trySerialization(result)}'")

                ApiSuccessResponse(version = version, id = id, result = result)
            }
    }

    abstract fun execute(node: JsonNode): Result<R?, Fail>
}

