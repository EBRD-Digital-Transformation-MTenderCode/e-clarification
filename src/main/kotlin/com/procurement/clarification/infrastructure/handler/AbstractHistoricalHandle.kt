package com.procurement.clarification.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.dao.HistoryDao
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.dto.ApiSuccessResponse
import com.procurement.clarification.model.dto.bpe.getId
import com.procurement.clarification.model.dto.bpe.getVersion
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.tryToObject

abstract class AbstractHistoricalHandler<ACTION : Action, R : Any>(
    private val target: Class<ApiSuccessResponse>,
    private val historyRepository: HistoryDao,
    private val logger: Logger
) : AbstractHandler<ACTION, ApiResponse>(logger = logger) {


    override fun handle(node: JsonNode): ApiResponse {

        val id = node.getId().get
        val version = node.getVersion().get

        val history = historyRepository.getHistory(id.toString(), action.key)
        if (history != null) {
            val data = history.jsonData
            val result = data.tryToObject(target)
                .doOnError {
                    return responseError(
                        id = id,
                        version = version,
                        fail = Fail.Incident.ParsingIncident()
                    )
                }
            return result.get
        }

        return when (val serviceResult = execute(node)) {
            is Result.Success -> ApiSuccessResponse(id = id, version = version, result = serviceResult.get)
                .also {
                    logger.info("'${action.key}' has been executed. Result: '${toJson(it)}'")
                    historyRepository.saveHistory(id.toString(), action.key, it)
                }
            is Result.Failure -> responseError(id = id, version = version, fail = serviceResult.error)
        }
    }

    abstract fun execute(node: JsonNode): Result<R, Fail>
}
