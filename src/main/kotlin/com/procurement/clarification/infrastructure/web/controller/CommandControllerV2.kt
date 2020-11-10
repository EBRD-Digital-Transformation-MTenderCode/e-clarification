package com.procurement.clarification.infrastructure.web.controller

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.error.BadRequestErrors
import com.procurement.clarification.infrastructure.api.ApiVersion
import com.procurement.clarification.infrastructure.api.v2.ApiResponseV2
import com.procurement.clarification.infrastructure.bind.api.CommandId
import com.procurement.clarification.infrastructure.api.v2.errorResponse
import com.procurement.clarification.infrastructure.api.v2.getId
import com.procurement.clarification.infrastructure.api.v2.getVersion
import com.procurement.clarification.infrastructure.service.CommandServiceV2
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.tryToNode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/command2")
class CommandControllerV2(
    private val command2Service: CommandServiceV2,
    private val logger: Logger
) {

    @PostMapping
    fun command(
        @RequestBody requestBody: String
    ): ResponseEntity<ApiResponseV2> {

        logger.info("RECEIVED COMMAND: '${requestBody}'.")

        val node = requestBody.tryToNode()
            .mapFailure {
                BadRequestErrors.Parsing(
                    message = "Invalid request data",
                    request = requestBody,
                    exception = it.exception
                )
            }
            .onFailure {
                return errorResponseEntity(
                    fail = it.reason,
                    id = CommandId.NaN,
                    version = ApiVersion.NaN,
                    logger = logger
                )
            }

        val version = node.getVersion()
            .onFailure {
                val id = node.getId().getOrElse(CommandId.NaN)
                return errorResponseEntity(
                    fail = it.reason,
                    id = id,
                    version = ApiVersion.NaN,
                    logger = logger
                )
            }

        val id = node.getId()
            .onFailure {
                return errorResponseEntity(fail = it.reason, version = version, id = CommandId.NaN, logger = logger)
            }

        val response = command2Service.execute(request = node)
            .also { response ->
                logger.info("RESPONSE (id: '${id}'): '${toJson(response)}'.")
            }

        return ResponseEntity(response, HttpStatus.OK)
    }

    fun errorResponseEntity(
        fail: Fail,
        id: CommandId,
        version: ApiVersion,
        logger: Logger
    ): ResponseEntity<ApiResponseV2> = errorResponse(fail = fail, version = version, id = id, logger = logger)
        .let { ResponseEntity(it, HttpStatus.OK) }
}
