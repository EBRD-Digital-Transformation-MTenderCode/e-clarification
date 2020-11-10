package com.procurement.clarification.infrastructure.handler

import com.procurement.clarification.infrastructure.model.CommandId
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Action
import com.procurement.clarification.infrastructure.repository.history.model.HistoryEntity
import com.procurement.clarification.lib.functional.Result

interface HistoryRepository {
    fun getHistory(commandId: CommandId, action: Action): Result<String?, Fail.Incident.Database.Interaction>
    fun saveHistory(entity: HistoryEntity): Result<HistoryEntity, Fail.Incident.Database.Interaction>
}
