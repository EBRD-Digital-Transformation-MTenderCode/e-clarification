package com.procurement.clarification.infrastructure.extension

import com.datastax.driver.core.BatchStatement
import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.Result.Companion.failure
import com.procurement.clarification.domain.util.Result.Companion.success


fun BoundStatement.tryExecute(session: Session): Result<ResultSet, Fail.Incident.Database> = try {
    success(session.execute(this))
} catch (expected: Exception) {
    failure(Fail.Incident.Database(exception = expected))
}

fun BatchStatement.tryExecute(session: Session): Result<ResultSet, Fail.Incident.Database> = try {
    success(session.execute(this))
} catch (expected: Exception) {
    failure(Fail.Incident.Database(exception = expected))
}