package com.procurement.clarification.infrastructure.repository.history.model

import com.procurement.clarification.infrastructure.bind.api.Action
import com.procurement.clarification.infrastructure.bind.api.CommandId
import java.time.LocalDateTime

data class HistoryEntity(
    var commandId: CommandId,
    var action: Action,
    var date: LocalDateTime,
    var data: String
)
