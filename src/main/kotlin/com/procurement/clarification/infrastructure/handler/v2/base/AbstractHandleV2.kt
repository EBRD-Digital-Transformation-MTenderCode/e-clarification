package com.procurement.clarification.infrastructure.handler.v2.base

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.fail.error.ValidationErrors
import com.procurement.clarification.infrastructure.bind.api.Action
import com.procurement.clarification.infrastructure.api.ApiVersion
import com.procurement.clarification.infrastructure.api.v2.ApiResponseV2
import com.procurement.clarification.infrastructure.bind.api.CommandId
import com.procurement.clarification.infrastructure.api.v2.generateDataErrorResponse
import com.procurement.clarification.infrastructure.api.v2.generateErrorResponse
import com.procurement.clarification.infrastructure.api.v2.generateIncidentResponse
import com.procurement.clarification.infrastructure.api.v2.generateValidationErrorResponse
import com.procurement.clarification.infrastructure.handler.Handler

abstract class AbstractHandler<ACTION : Action, R : Any>(
    private val logger: Logger
) : Handler<ACTION, ApiResponseV2> {

    protected fun responseError(id: CommandId, version: ApiVersion, fail: Fail): ApiResponseV2 {
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
