package com.procurement.clarification.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.clarification.domain.EnumElementProvider

enum class OperationType(@JsonValue override val key: String) : EnumElementProvider.Key {

    CREATE_PCR("createPcr");

    override fun toString(): String = key

    companion object : EnumElementProvider<OperationType>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = orThrow(name)
    }
}
