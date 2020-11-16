package com.procurement.clarification.infrastructure.api.v2

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.clarification.domain.EnumElementProvider
import com.procurement.clarification.infrastructure.bind.api.Action

enum class CommandTypeV2(@JsonValue override val key: String) : EnumElementProvider.Key, Action {
    CREATE_ENQUIRY_PERIOD("createEnquiryPeriod"),
    FIND_ENQUIRIES("findEnquiries"),
    FIND_ENQUIRY_IDS("findEnquiryIds"),
    ;

    override fun toString(): String = key

    companion object : EnumElementProvider<CommandTypeV2>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = CommandTypeV2.orThrow(name)
    }
}
