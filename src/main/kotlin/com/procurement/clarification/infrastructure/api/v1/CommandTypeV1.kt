package com.procurement.clarification.infrastructure.api.v1

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.clarification.infrastructure.bind.api.Action

enum class CommandTypeV1(override val key: String) : Action {
    ADD_ANSWER("addAnswer"),
    CHECK_ANSWER("checkAnswer"),
    CHECK_ENQUIRIES("checkEnquiries"),
    CHECK_PERIOD("checkPeriod"),
    CREATE_ENQUIRY("createEnquiry"),
    CREATE_PERIOD("createPeriod"),
    SAVE_NEW_PERIOD("saveNewPeriod"),
    SAVE_PERIOD("savePeriod"),
    VALIDATE_PERIOD("validatePeriod"),
    ;

    @JsonValue
    fun value(): String {
        return this.key
    }

    override fun toString(): String {
        return this.key
    }
}
