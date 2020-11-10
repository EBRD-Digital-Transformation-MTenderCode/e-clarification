package com.procurement.clarification.controller

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.config.GlobalProperties
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.error.BadRequestErrors
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.dto.ApiVersion
import com.procurement.clarification.infrastructure.model.CommandId
import com.procurement.clarification.model.dto.bpe.errorResponse
import com.procurement.clarification.model.dto.bpe.getId
import com.procurement.clarification.model.dto.bpe.getVersion
import com.procurement.clarification.service.Command2Service
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
class Command2Controller(
    private val command2Service: Command2Service,
    private val logger: Logger
) {

    @PostMapping
    fun command(
        @RequestBody requestBody: String
    ): ResponseEntity<ApiResponse> {

        logger.info("RECEIVED COMMAND: '${requestBody}'.")

        val node = requestBody.tryToNode()
            .onFailure {
                return responseEntity(
                    fail = BadRequestErrors.Parsing(
                        message = "Invalid request data",
                        request = requestBody,
                        exception = it.reason.exception
                    ),
                    id = CommandId.NaN
                )
            }

        val version = node.getVersion()
            .onFailure {
                val id = node.getId().getOrElse(CommandId.NaN)
                return responseEntity(fail = it.reason, id = id)
            }

        val id = node.getId()
            .onFailure { return responseEntity(fail = it.reason, version = version, id = CommandId.NaN) }

        val response = command2Service.execute(request = node)
            .also { response ->
                logger.info("RESPONSE (id: '${id}'): '${toJson(response)}'.")
            }

        return ResponseEntity(response, HttpStatus.OK)
    }

    private fun responseEntity(
        fail: Fail,
        id: CommandId,
        version: ApiVersion = GlobalProperties.App.apiVersion
    ): ResponseEntity<ApiResponse> {
        fail.logging(logger)
        val response = errorResponse(fail = fail, id = id, version = version)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
