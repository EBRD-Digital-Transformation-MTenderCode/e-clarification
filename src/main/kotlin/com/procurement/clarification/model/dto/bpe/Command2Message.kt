package com.procurement.clarification.model.dto.bpe

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.node.NullNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.config.GlobalProperties
import com.procurement.clarification.domain.EnumElementProvider
import com.procurement.clarification.domain.EnumElementProvider.Companion.keysAsStrings
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.Fail.Error
import com.procurement.clarification.domain.fail.error.BadRequestErrors
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.fail.error.ValidationErrors
import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.domain.util.extension.toList
import com.procurement.clarification.infrastructure.dto.ApiVersion
import com.procurement.clarification.infrastructure.handler.model.ApiResponseV2
import com.procurement.clarification.infrastructure.model.CommandId
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.Result.Companion.failure
import com.procurement.clarification.lib.functional.Result.Companion.success
import com.procurement.clarification.lib.functional.asFailure
import com.procurement.clarification.lib.functional.asSuccess
import com.procurement.clarification.lib.functional.flatMap
import com.procurement.clarification.utils.tryToObject
import java.time.LocalDateTime
import java.util.*

enum class Command2Type(@JsonValue override val key: String) : EnumElementProvider.Key, Action {
    CREATE_ENQUIRY_PERIOD("createEnquiryPeriod"),
    FIND_ENQUIRIES("findEnquiries"),
    FIND_ENQUIRY_IDS("findEnquiryIds"),
    ;

    override fun toString(): String = key

    companion object : EnumElementProvider<Command2Type>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = Command2Type.orThrow(name)
    }
}

fun errorResponse(fail: Fail, id: CommandId, version: ApiVersion = GlobalProperties.App.apiVersion, logger: Logger): ApiResponseV2 {
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
            id = UUID.randomUUID().toString(),
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

fun JsonNode.getId(): Result<CommandId, DataErrors> = tryGetStringAttribute("id").map { value -> CommandId(value) }

fun JsonNode.getVersion(): Result<ApiVersion, DataErrors> =
    tryGetStringAttribute("version")
        .flatMap { version ->
            ApiVersion.orNull(version)
                ?.asSuccess()
                ?: DataErrors.Validation.DataFormatMismatch(
                    name = "version",
                    expectedFormat = ApiVersion.pattern,
                    actualValue = version
                ).asFailure()
        }

fun JsonNode.getAction(): Result<Command2Type, DataErrors> =
    tryGetEnumAttribute(name = "action", enumProvider = Command2Type)

private fun JsonNode.tryGetStringAttribute(name: String): Result<String, DataErrors> =
    tryGetAttribute(name = name, type = JsonNodeType.STRING)
        .map { it.asText() }

private fun <T> JsonNode.tryGetEnumAttribute(name: String, enumProvider: EnumElementProvider<T>)
    : Result<T, DataErrors> where T : Enum<T>,
                                  T : EnumElementProvider.Key =
    this.tryGetStringAttribute(name)
        .flatMap { enum ->
            enumProvider.orNull(enum)
                ?.asSuccess<T, DataErrors>()
                ?: failure(
                    DataErrors.Validation.UnknownValue(
                        name = name,
                        expectedValues = enumProvider.allowedElements.keysAsStrings(),
                        actualValue = enum
                    )
                )
        }

private fun JsonNode.tryGetAttribute(name: String, type: JsonNodeType): Result<JsonNode, DataErrors> =
    getAttribute(name = name)
        .flatMap { node ->
            if (node.nodeType == type)
                success(node)
            else
                failure(
                    DataErrors.Validation.DataTypeMismatch(
                        name = name,
                        expectedType = type.name,
                        actualType = node.nodeType.name
                    )
                )
        }

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

fun JsonNode.getAttribute(name: String): Result<JsonNode, DataErrors> {
    return if (has(name)) {
        val attr = get(name)
        if (attr !is NullNode)
            success(attr)
        else
            failure(DataErrors.Validation.DataTypeMismatch(name = name, actualType = "null", expectedType = "not null"))
    } else
        failure(DataErrors.Validation.MissingRequiredAttribute(name = name))
}

fun JsonNode.tryGetParams(): Result<JsonNode, DataErrors> =
    getAttribute("params")
