package com.procurement.clarification.dao

import com.datastax.driver.core.Session
import com.procurement.clarification.application.repository.PeriodRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.lib.functional.MaybeFail
import com.procurement.clarification.infrastructure.extension.cassandra.tryExecute
import com.procurement.clarification.model.entity.PeriodEntity
import org.springframework.stereotype.Repository

@Repository
class CassandraPeriodRepository(private val session: Session) : PeriodRepository {

    companion object {
        private const val KEYSPACE = "ocds"
        private const val PERIOD_TABLE = "clarification_period"
        private const val COLUMN_CP_ID = "cp_id"
        private const val COLUMN_STAGE = "stage"
        private const val COLUMN_OWNER = "owner"
        private const val COLUMN_START_DATE = "start_date"
        private const val COLUMN_END_DATE = "end_date"
        private const val COLUMN_TENDER_END_DATE = "tender_end_date"

        private const val SAVE_CQL = """
               INSERT INTO $KEYSPACE.$PERIOD_TABLE(
                   $COLUMN_CP_ID,
                   $COLUMN_STAGE,
                   $COLUMN_OWNER,
                   $COLUMN_START_DATE,
                   $COLUMN_END_DATE,
                   $COLUMN_TENDER_END_DATE
               )
               VALUES(?, ?, ?, ?, ?, ?)
            """
    }

    private val preparedSave = session.prepare(SAVE_CQL)

    override fun save(
        period: PeriodEntity
    ): MaybeFail<Fail.Incident> {
        val statement = preparedSave.bind()
            .apply {
                setString(COLUMN_CP_ID, period.cpId)
                setString(COLUMN_STAGE, period.stage)
                setString(COLUMN_OWNER, period.owner)
                setTimestamp(COLUMN_START_DATE, period.startDate)
                setTimestamp(COLUMN_END_DATE, period.endDate)
                setTimestamp(COLUMN_TENDER_END_DATE, period.tenderEndDate)
            }

        statement.tryExecute(session)
            .doOnError { fail -> return MaybeFail.fail(fail) }

        return MaybeFail.none()
    }
}
