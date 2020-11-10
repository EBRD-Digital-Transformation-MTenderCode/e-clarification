package com.procurement.clarification.infrastructure.handler

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.fail.error.ValidationErrors
import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.dto.ApiVersion
import com.procurement.clarification.infrastructure.model.CommandId
import com.procurement.clarification.model.dto.bpe.generateDataErrorResponse
import com.procurement.clarification.model.dto.bpe.generateErrorResponse
import com.procurement.clarification.model.dto.bpe.generateIncidentResponse
import com.procurement.clarification.model.dto.bpe.generateValidationErrorResponse

abstract class AbstractHandler<ACTION : Action, R : Any>(
    private val logger: Logger
) : Handler<ACTION, ApiResponse> {

    protected fun responseError(id: CommandId, version: ApiVersion, fail: Fail): ApiResponse {
        fail.logging(logger)
        return when (fail) {
            is Fail.Error -> {
                when (fail) {
                    is DataErrors.Validation -> generateDataErrorResponse(id = id, version = version, fail = fail)
                    is ValidationErrors -> generateValidationErrorResponse(id = id, version = version, fail = fail)
                    else -> generateErrorResponse(id = id, version = version, fail = fail)
                }
            }
            is Fail.Incident -> generateIncidentResponse(id = id, version = version, fail = fail)
        }
    }
}
