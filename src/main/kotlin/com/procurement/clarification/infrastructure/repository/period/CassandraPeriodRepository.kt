package com.procurement.clarification.infrastructure.repository.period

import com.datastax.driver.core.Session
import com.procurement.clarification.application.repository.period.PeriodRepository
import com.procurement.clarification.application.repository.period.model.PeriodEntity
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.infrastructure.extension.cassandra.toCassandraTimestamp
import com.procurement.clarification.infrastructure.extension.cassandra.toLocalDateTime
import com.procurement.clarification.infrastructure.extension.cassandra.tryExecute
import com.procurement.clarification.infrastructure.repository.Database
import com.procurement.clarification.lib.functional.MaybeFail
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asSuccess
import org.springframework.stereotype.Repository

@Repository
class CassandraPeriodRepository(private val session: Session) : PeriodRepository {

    companion object {

        private const val SAVE_CQL = """
               INSERT INTO ${Database.KEYSPACE}.${Database.Period.TABLE}(
                   ${Database.Period.CPID},
                   ${Database.Period.OCID},
                   ${Database.Period.OWNER},
                   ${Database.Period.START_DATE},
                   ${Database.Period.END_DATE}
               )
               VALUES(?, ?, ?, ?, ?)
            """

        private const val GET_CQL = """
               SELECT ${Database.Period.OWNER},
                      ${Database.Period.START_DATE},
                      ${Database.Period.END_DATE}
                 FROM ${Database.KEYSPACE}.${Database.Period.TABLE}
                WHERE ${Database.Period.CPID}=?
                  AND ${Database.Period.OCID}=?
            """
    }

    private val preparedSave = session.prepare(SAVE_CQL)
    private val preparedGetCQL = session.prepare(GET_CQL)

    override fun find(cpid: Cpid, ocid: Ocid): Result<PeriodEntity?, Fail.Incident.Database.Interaction> =
        preparedGetCQL.bind()
            .apply {
                setString(Database.Period.CPID, cpid.toString())
                setString(Database.Period.OCID, ocid.toString())
            }
            .tryExecute(session)
            .onFailure { return it }
            .one()
            ?.let { row ->
                PeriodEntity(
                    cpid = cpid,
                    ocid = ocid,
                    owner = row.getString(Database.Period.OWNER),
                    startDate = row.getTimestamp(Database.Period.START_DATE).toLocalDateTime(),
                    endDate = row.getTimestamp(Database.Period.END_DATE).toLocalDateTime()
                )
            }
            .asSuccess()

    override fun save(period: PeriodEntity): MaybeFail<Fail.Incident> {
        preparedSave.bind()
            .apply {
                setString(Database.Period.CPID, period.cpid.underlying)
                setString(Database.Period.OCID, period.ocid.underlying)
                setString(Database.Period.OWNER, period.owner)
                setTimestamp(Database.Period.START_DATE, period.startDate.toCassandraTimestamp())
                setTimestamp(Database.Period.END_DATE, period.endDate.toCassandraTimestamp())
            }
            .tryExecute(session)
            .onFailure { return MaybeFail.fail(it.reason) }

        return MaybeFail.none()
    }
}
