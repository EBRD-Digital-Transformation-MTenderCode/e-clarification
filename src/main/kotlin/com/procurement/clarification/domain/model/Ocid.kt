package com.procurement.clarification.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.clarification.domain.EnumElementProvider.Companion.keysAsStrings
import com.procurement.clarification.domain.model.enums.Stage
import java.io.Serializable

class Ocid private constructor(val underlying: String, val stage: Stage) : Serializable {

    override fun equals(other: Any?): Boolean {
        return if (this !== other)
            other is Ocid
                && this.underlying == other.underlying
        else
            true
    }

    override fun hashCode(): Int = underlying.hashCode()

    @JsonValue
    override fun toString(): String = underlying

    companion object {
        private const val STAGE_POSITION = 4
        private val STAGES: String
            get() = Stage.allowedElements.keysAsStrings()
                .joinToString(separator = "|", prefix = "(", postfix = ")") { it.toUpperCase() }

        private val regex = "^[a-z]{4}-[a-z0-9]{6}-[A-Z]{2}-[0-9]{13}-$STAGES-[0-9]{13}\$".toRegex()

        val pattern: String
            get() = regex.pattern

        @JvmStatic
        @JsonCreator
        fun tryCreateOrNull(value: String): Ocid? =
            if (value.matches(regex)) {
                val stage = Stage.orNull(value.split("-")[STAGE_POSITION])!!
                Ocid(underlying = value, stage = stage)
            } else
                null
    }
}
