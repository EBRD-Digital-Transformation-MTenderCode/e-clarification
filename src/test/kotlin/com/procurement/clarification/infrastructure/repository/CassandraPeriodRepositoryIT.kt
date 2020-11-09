package com.procurement.clarification.infrastructure.repository

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.HostDistance
import com.datastax.driver.core.PlainTextAuthProvider
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.nhaarman.mockito_kotlin.spy
import com.procurement.clarification.application.repository.PeriodRepository
import com.procurement.clarification.dao.CassandraPeriodRepository
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.enums.OperationType
import com.procurement.clarification.domain.model.enums.ProcurementMethod
import com.procurement.clarification.infrastructure.configuration.DatabaseTestConfiguration
import com.procurement.clarification.model.entity.PeriodEntity
import com.procurement.clarification.utils.toDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DatabaseTestConfiguration::class])
class CassandraPeriodRepositoryIT {

    companion object {
        private const val KEYSPACE = "ocds"
        private const val PERIOD_TABLE = "clarification_period"
        private const val COLUMN_CP_ID = "cp_id"
        private const val COLUMN_STAGE = "stage"
        private const val COLUMN_OWNER = "owner"
        private const val COLUMN_START_DATE = "start_date"
        private const val COLUMN_END_DATE = "end_date"
        private const val COLUMN_TENDER_END_DATE = "tender_end_date"

        private const val COUNTRY = "MD"
        private val PMD = ProcurementMethod.CF
        private val OPERATION_TYPE = OperationType.CREATE_PCR
        private val CPID = Cpid.tryCreateOrNull("ocds-t1s2t3-MD-1565251033096")!!
        private val OCID = Ocid.tryCreateOrNull("ocds-b3wdp1-MD-1581509539187-EV-1581509653044")!!

        private const val FORMAT_PATTERN = "uuuu-MM-dd'T'HH:mm:ss'Z'"
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN)
            .withResolverStyle(ResolverStyle.STRICT)
        private val START_DATE = LocalDateTime.parse("2020-02-12T08:49:55Z", FORMATTER)
        private val END_DATE = LocalDateTime.parse("2020-02-22T08:49:55Z", FORMATTER)
    }

    @Autowired
    private lateinit var container: CassandraTestContainer

    private lateinit var session: Session
    private lateinit var periodRepository: PeriodRepository

    @BeforeEach
    fun init() {
        val poolingOptions = PoolingOptions()
            .setMaxConnectionsPerHost(HostDistance.LOCAL, 1)
        val cluster = Cluster.builder()
            .addContactPoints(container.contractPoint)
            .withPort(container.port)
            .withoutJMXReporting()
            .withPoolingOptions(poolingOptions)
            .withAuthProvider(PlainTextAuthProvider(container.username, container.password))
            .build()

        session = spy(cluster.connect())

        createKeyspace()
        createTable()

        periodRepository = CassandraPeriodRepository(session)
    }

    @AfterEach
    fun clean() {
        dropKeyspace()
    }

    @Test
    fun save_success() {
        val period = PeriodEntity(
            cpId = CPID.toString(),
            stage = OCID.stage.toString(),
            owner = UUID.randomUUID().toString(),
            startDate = START_DATE.toDate(),
            endDate = END_DATE.toDate(),
            tenderEndDate = null
        )

        periodRepository.save(period)

        val savedPeriod = findBy(cpid = period.cpId, stage = period.stage)

        assertEquals(period, savedPeriod)
    }

    private fun findBy(cpid: String, stage: String): PeriodEntity? {
        val select = QueryBuilder.select()
            .from(KEYSPACE, PERIOD_TABLE)
            .where(QueryBuilder.eq(COLUMN_CP_ID, cpid))
            .and(QueryBuilder.eq(COLUMN_STAGE, stage))
        val row = session.execute(select).one()

        return PeriodEntity(
            cpId = row.getString(COLUMN_CP_ID),
            owner = row.getString(COLUMN_OWNER),
            tenderEndDate = row.getTimestamp(COLUMN_TENDER_END_DATE),
            endDate = row.getTimestamp(COLUMN_END_DATE),
            startDate = row.getTimestamp(COLUMN_START_DATE),
            stage = row.getString(COLUMN_STAGE)
        )
    }

    private fun createKeyspace() {
        session.execute(
            "CREATE KEYSPACE $KEYSPACE " +
                "WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};"
        )
    }

    private fun dropKeyspace() {
        session.execute("DROP KEYSPACE $KEYSPACE;")
    }

    private fun createTable() {
        session.execute(
            """
                CREATE TABLE IF NOT EXISTS  $KEYSPACE.$PERIOD_TABLE (
                     $COLUMN_CP_ID text,
                     $COLUMN_STAGE text,
                     $COLUMN_OWNER text,
                     $COLUMN_START_DATE timestamp,
                     $COLUMN_END_DATE timestamp,
                     $COLUMN_TENDER_END_DATE timestamp,
                     primary key($COLUMN_CP_ID, $COLUMN_STAGE)
                    );
            """
        )
    }
}