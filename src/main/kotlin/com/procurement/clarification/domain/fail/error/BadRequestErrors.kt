package com.procurement.clarification.domain.fail.error

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail

sealed class BadRequestErrors(
    numberError: String,
    override val description: String
) : Fail.Error("RQ-") {

    override val code: String = prefix + numberError

    class Parsing(message: String, val request: String, val exception: Exception? = null) : BadRequestErrors(
        numberError = "01",
        description = message
    ) {
        override fun logging(logger: Logger) {
            logger.error(message = "$message Invalid request body $request.", exception = exception)
        }
    }

    override fun logging(logger: Logger) {
        logger.error(message = message)
    }
}
