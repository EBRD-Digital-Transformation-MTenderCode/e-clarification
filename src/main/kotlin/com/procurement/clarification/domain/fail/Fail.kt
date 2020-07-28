package com.procurement.clarification.domain.fail

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.EnumElementProvider
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.ValidationResult

sealed class Fail {

    abstract fun logging(logger: Logger)

    abstract class Error(val prefix: String) : Fail() {
        abstract val code: String
        abstract val description: String
        val message: String
            get() = "ERROR CODE: '$code', DESCRIPTION: '$description'."

        override fun logging(logger: Logger) {
            logger.error(message = message)
        }

        companion object {
            fun <T, E : Error> E.toResult(): Result<T, E> = Result.failure(this)

            fun <E : Error> E.toValidationResult(): ValidationResult<E> = ValidationResult.error(this)
        }
    }

    sealed class Incident(val level: Level, number: String, val description: String) : Fail() {
        val code: String = "INC-$number"

        val message: String
            get() = "INCIDENT CODE: '$code', DESCRIPTION: '$description'."

        override fun logging(logger: Logger) {
            when (level) {
                Level.ERROR -> logger.error(message)
                Level.WARNING -> logger.warn(message)
                Level.INFO -> logger.info(message)
            }
        }

        class Database(val exception: Exception) : Incident(
            level = Level.ERROR,
            number = "01",
            description = "Database incident."
        ) {
            override fun logging(logger: Logger) {
                logger.error(message = message, exception = exception)
            }
        }

        class Parsing(val className: String, val exception: Exception) : Incident(
            level = Level.ERROR,
            number = "02",
            description = "Error parsing to $className."
        ) {
            override fun logging(logger: Logger) {
                logger.error(message = message, exception = exception)
            }
        }

        class DatabaseIncident(val exception: Exception? = null) : Incident(
            level = Level.ERROR,
            number = "03",
            description = "Internal Server Error."
        ) {
            override fun logging(logger: Logger) {
                logger.error(message = message, exception = exception)
            }
        }

        class Transforming(val exception: Exception) :
            Incident(
                level = Level.ERROR,
                number = "04",
                description = "Error transforming."
            ) {
            override fun logging(logger: Logger) {
                logger.error(message = message, exception = exception)
            }
        }

        class ParsingIncident : Incident(
            level = Level.ERROR,
            number = "05",
            description = "Internal Server Error."
        ) {
            override fun logging(logger: Logger) {
                logger.error(message = message)
            }
        }

        enum class Level(@JsonValue override val key: String) : EnumElementProvider.Key {
            ERROR("error"),
            WARNING("warning"),
            INFO("info");

            companion object : EnumElementProvider<Level>(info = info())
        }
    }
}