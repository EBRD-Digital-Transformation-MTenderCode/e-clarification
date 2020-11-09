package com.procurement.clarification.domain.fail

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.EnumElementProvider
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.ValidationResult

sealed class Fail {

    abstract val code: String
    abstract val description: String

    abstract fun logging(logger: Logger)

    abstract class Error(val prefix: String) : Fail() {
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

    sealed class Incident(val level: Level, number: String, override val description: String) : Fail() {
        override val code: String = "INC-$number"

        val message: String
            get() = "INCIDENT CODE: '$code', DESCRIPTION: '$description'."

        override fun logging(logger: Logger) {
            when (level) {
                Level.ERROR -> logger.error(message)
                Level.WARNING -> logger.warn(message)
                Level.INFO -> logger.info(message)
            }
        }

        sealed class Database(val number: String, override val description: String) :
            Incident(level = Level.ERROR, number = number, description = description) {

            abstract val exception: Exception

            class Interaction(override val exception: Exception) :
                Database(number = "1.1", description = "Database incident.") {

                override fun logging(logger: Logger) {
                    logger.error(message = message, exception = exception)
                }
            }

            class Parsing(val column: String, val value: String, override val exception: Exception) :
                Database(
                    number = "1.4",
                    description = "Could not parse data stored in database."
                ) {

                override fun logging(logger: Logger) {
                    logger.error(
                        message = message,
                        mdc = mapOf("column" to column, "value" to value),
                        exception = exception
                    )
                }
            }

            class DatabaseParsing(override val exception: Exception) : Database(
                number = "1.5",
                description = "Internal Server Error.",
            ) {

                override fun logging(logger: Logger) {
                    logger.error(message = message, exception = exception)
                }
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


        enum class Level(@JsonValue override val key: String) : EnumElementProvider.Key {
            ERROR("error"),
            WARNING("warning"),
            INFO("info");

            companion object : EnumElementProvider<Level>(info = info())
        }
    }
}
