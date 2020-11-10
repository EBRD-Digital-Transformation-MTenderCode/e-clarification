package com.procurement.clarification.infrastructure.api.v2

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.infrastructure.configuration.properties.GlobalProperties
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.Fail.Error
import com.procurement.clarification.domain.fail.error.BadRequestErrors
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.fail.error.ValidationErrors
import com.procurement.clarification.domain.util.extension.toList
import com.procurement.clarification.infrastructure.api.ApiVersion
import com.procurement.clarification.infrastructure.bind.api.CommandId
import com.procurement.clarification.infrastructure.extension.jackson.tryGetAttribute
import com.procurement.clarification.infrastructure.extension.jackson.tryGetAttributeAsEnum
import com.procurement.clarification.infrastructure.extension.jackson.tryGetTextAttribute
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asFailure
import com.procurement.clarification.lib.functional.asSuccess
import com.procurement.clarification.lib.functional.flatMap
import com.procurement.clarification.utils.tryToObject
import java.time.LocalDateTime

fun errorResponse(
    fail: Fail,
    id: CommandId,
    version: ApiVersion = GlobalProperties.App.apiVersion,
    logger: Logger
): ApiResponseV2 {
    fail.logging(logger)
    return when (fail) {
        is DataErrors.Validation -> generateDataErrorResponse(id = id, version = version, fail = fail)
        is Error -> generateErrorResponse(id = id, version = version, fail = fail)
        is Fail.Incident -> generateIncidentResponse(id = id, version = version, fail = fail)
    }
}

fun generateDataErrorResponse(id: CommandId, version: ApiVersion, fail: DataErrors.Validation) =
    ApiResponseV2.Error(
        version = version,
        id = id,
        result = listOf(
            ApiResponseV2.Error.Result(
                code = "${fail.code}/${GlobalProperties.service.id}",
                description = fail.description,
                details = ApiResponseV2.Error.Result.Detail.tryCreateOrNull(name = fail.name).toList()
            )
        )
    )

fun generateValidationErrorResponse(id: CommandId, version: ApiVersion, fail: ValidationErrors) =
    ApiResponseV2.Error(
        version = version,
        id = id,
        result = listOf(
            ApiResponseV2.Error.Result(
                code = "${fail.code}/${GlobalProperties.service.id}",
                description = fail.description,
                details = ApiResponseV2.Error.Result.Detail.tryCreateOrNull(id = fail.entityId).toList()
            )
        )
    )

fun generateErrorResponse(id: CommandId, version: ApiVersion, fail: Error) =
    ApiResponseV2.Error(
        version = version,
        id = id,
        result = listOf(
            ApiResponseV2.Error.Result(
                code = "${fail.code}/${GlobalProperties.service.id}",
                description = fail.description
            )
        )
    )

fun generateIncidentResponse(id: CommandId, version: ApiVersion, fail: Fail.Incident) =
    ApiResponseV2.Incident(
        id = id,
        version = version,
        result = ApiResponseV2.Incident.Result(
            id = IncidentId.generate(),
            date = LocalDateTime.now(),
            level = fail.level,
            details = listOf(
                ApiResponseV2.Incident.Result.Detail(
                    code = "${fail.code}/${GlobalProperties.service.id}",
                    description = fail.description,
                    metadata = null
                )
            ),
            service = ApiResponseV2.Incident.Result.Service(
                id = GlobalProperties.service.id,
                version = GlobalProperties.service.version,
                name = GlobalProperties.service.name
            )
        )
    )

fun JsonNode.getId(): Result<CommandId, DataErrors> = tryGetTextAttribute("id").map { value -> CommandId(value) }

fun JsonNode.getVersion(): Result<ApiVersion, DataErrors> =
    tryGetTextAttribute("version")
        .flatMap { version ->
            ApiVersion.orNull(version)
                ?.asSuccess()
                ?: DataErrors.Validation.DataFormatMismatch(
                    name = "version",
                    expectedFormat = ApiVersion.pattern,
                    actualValue = version
                ).asFailure()
        }

fun JsonNode.getAction(): Result<CommandTypeV2, DataErrors> =
    tryGetAttributeAsEnum(name = "action", enumProvider = CommandTypeV2)

fun <T : Any> JsonNode.tryParamsToObject(clazz: Class<T>): Result<T, Error> {
    return this.tryToObject(clazz)
        .doOnError { error ->
            return Result.failure(
                BadRequestErrors.Parsing(
                    message = "Can not parse 'params'.",
                    request = this.toString(),
                    exception = error.exception
                )
            )
        }
        .get
        .asSuccess()
}

fun JsonNode.tryGetParams(): Result<JsonNode, DataErrors> =
    tryGetAttribute("params")
