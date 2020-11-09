package com.procurement.clarification.infrastructure.repository.history.model

import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.infrastructure.model.CommandId
import java.time.LocalDateTime

data class HistoryEntity(
    var commandId: CommandId,
    var action: Action,
    var date: LocalDateTime,
    var data: String
)
