package com.procurement.clarification.infrastructure.handler

import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.infrastructure.bind.api.Action
import com.procurement.clarification.infrastructure.bind.api.CommandId
import com.procurement.clarification.infrastructure.repository.history.model.HistoryEntity
import com.procurement.clarification.lib.functional.Result

interface HistoryRepository {
    fun getHistory(commandId: CommandId, action: Action): Result<String?, Fail.Incident.Database.Interaction>
    fun saveHistory(entity: HistoryEntity): Result<HistoryEntity, Fail.Incident.Database.Interaction>
}
