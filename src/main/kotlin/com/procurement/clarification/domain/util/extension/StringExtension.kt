package com.procurement.clarification.domain.util.extension

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asFailure
import com.procurement.clarification.domain.util.asSuccess
import java.util.*

fun String.tryUUID(): Result<UUID, Fail.Incident.Parsing> =
    try {
        UUID.fromString(this).asSuccess()
    } catch (ex: Exception) {
        Fail.Incident.Parsing(UUID::class.java.canonicalName, ex).asFailure()
    }