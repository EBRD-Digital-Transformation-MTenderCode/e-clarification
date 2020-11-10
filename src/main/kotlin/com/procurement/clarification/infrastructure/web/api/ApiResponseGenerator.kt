package com.procurement.clarification.infrastructure.web.api

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.config.GlobalProperties
import com.procurement.clarification.domain.extension.nowDefaultUTC
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.util.extension.toListOrEmpty
import com.procurement.clarification.infrastructure.dto.ApiErrorResponse
import com.procurement.clarification.infrastructure.dto.ApiIncidentResponse
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.dto.ApiVersion
import com.procurement.clarification.infrastructure.model.CommandId
import java.util.*

object ApiResponseGenerator {

    fun generateResponseOnFailure(
        fail: Fail,
        version: ApiVersion,
        id: CommandId,
        logger: Logger
    ): ApiResponse {
        fail.logging(logger)
        return when (fail) {
            is Fail.Error -> when (fail) {
                is DataErrors.Validation -> generateDataErrorResponse(id = id, version = version, dataError = fail)
                else -> generateErrorResponse(id = id, version = version, error = fail)
            }

            is Fail.Incident -> generateIncidentResponse(id = id, version = version, incident = fail)
        }
    }

    private fun generateDataErrorResponse(dataError: DataErrors.Validation, version: ApiVersion, id: CommandId) =
        ApiErrorResponse(
            version = version,
            id = id,
            result = listOf(
                ApiErrorResponse.Error(
                    code = getFullErrorCode(dataError.code),
                    description = dataError.description,
                    details = ApiErrorResponse.Error.Detail.tryCreateOrNull(
                        name = dataError.name
                    ).toListOrEmpty()
                )
            )
        )

    private fun generateErrorResponse(version: ApiVersion, id: CommandId, error: Fail.Error) =
        ApiErrorResponse(
            version = version,
            id = id,
            result = listOf(
                ApiErrorResponse.Error(
                    code = getFullErrorCode(error.code),
                    description = error.description
                )
            )
        )

    private fun generateIncidentResponse(incident: Fail.Incident, version: ApiVersion, id: CommandId) =
        ApiIncidentResponse(
            version = version,
            id = id,
            result = ApiIncidentResponse.Incident(
                date = nowDefaultUTC(),
                id = UUID.randomUUID(),
                level = incident.level,
                service = ApiIncidentResponse.Incident.Service(
                    id = GlobalProperties.service.id,
                    version = GlobalProperties.service.version,
                    name = GlobalProperties.service.name
                ),
                details = listOf(
                    ApiIncidentResponse.Incident.Detail(
                        code = getFullErrorCode(incident.code),
                        description = incident.description,
                        metadata = null
                    )
                )
            )
        )

    private fun getFullErrorCode(code: String): String = "${code}/${GlobalProperties.service.id}"
}