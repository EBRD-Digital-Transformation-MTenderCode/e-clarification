package com.procurement.clarification.domain.model

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.domain.util.extension.tryUUID
import java.util.*

typealias Owner = UUID

fun String.tryOwner(): Result<Owner, Fail.Incident.Parsing> =
    this.tryUUID()