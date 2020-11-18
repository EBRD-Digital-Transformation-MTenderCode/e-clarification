package com.procurement.clarification.domain.fail.error

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail

sealed class GeneralValidationErrors(
    numberError: String,
    override val description: String,
) : Fail.Error(prefix = "VR-") {

    override val code: String = prefix + numberError

    override fun logging(logger: Logger) {
        logger.error(message = message)
    }

    class EntityNotFound(entityName: String, searchParams: Map<String, Any>) : GeneralValidationErrors(
        numberError = "17",
        description = "Entity not found. Cannot found $entityName by parameters: $searchParams",
    )

}
