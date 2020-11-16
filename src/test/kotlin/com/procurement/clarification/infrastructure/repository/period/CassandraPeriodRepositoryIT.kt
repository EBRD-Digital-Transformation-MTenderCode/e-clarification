package com.procurement.clarification.infrastructure.repository.period

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.HostDistance
import com.datastax.driver.core.PlainTextAuthProvider
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.Session
import com.nhaarman.mockito_kotlin.spy
import com.procurement.clarification.application.repository.period.PeriodRepository
import com.procurement.clarification.application.repository.period.model.PeriodEntity
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.get
import com.procurement.clarification.infrastructure.configuration.DatabaseTestConfiguration
import com.procurement.clarification.infrastructure.repository.CassandraTestContainer
import com.procurement.clarification.infrastructure.repository.Database
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DatabaseTestConfiguration::class])
class CassandraPeriodRepositoryIT {

    companion object {
        private val CPID = Cpid.tryCreateOrNull("ocds-t1s2t3-MD-1565251033096")!!
        private val OCID = Ocid.tryCreateOrNull("ocds-b3wdp1-MD-1581509539187-EV-1581509653044")!!
        private val START_DATE = LocalDateTime.now()
        private val END_DATE = START_DATE.plusDays(1)

        private val PERIOD_ENTITY = PeriodEntity(
            cpid = CPID,
            ocid = OCID,
            owner = UUID.randomUUID().toString(),
            startDate = START_DATE,
            endDate = END_DATE
        )
    }

    @Autowired
    private lateinit var container: CassandraTestContainer

    private lateinit var session: Session
    private lateinit var repository: PeriodRepository

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

        repository = CassandraPeriodRepository(session)
    }

    @AfterEach
    fun clean() {
        dropKeyspace()
    }

    @Test
    fun save_success() {
        repository.save(PERIOD_ENTITY)

        val fundedPeriod = repository.find(cpid = CPID, ocid = OCID).get()

        assertNotNull(fundedPeriod)
        assertEquals(PERIOD_ENTITY, fundedPeriod)
    }

    @Test
    fun find() {

        val result = repository.save(PERIOD_ENTITY)
        assertFalse(result.isFail)

        val loadedResult = repository.find(cpid = CPID, ocid = OCID)

        assertTrue(loadedResult.isSuccess)
        loadedResult.forEach {
            assertNotNull(it)
            assertEquals(CPID, it!!.cpid)
            assertEquals(OCID, it.ocid)
            assertEquals(START_DATE, it.startDate)
            assertEquals(END_DATE, it.endDate)
        }
    }

    @Test
    fun noFunded() {

        val loadedResult = repository.find(cpid = CPID, ocid = OCID)

        assertTrue(loadedResult.isSuccess)
        loadedResult.forEach {
            Assertions.assertNull(it)
        }
    }

    private fun createKeyspace() {
        session.execute(
            "CREATE KEYSPACE ${Database.KEYSPACE} " +
                "WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};"
        )
    }

    private fun dropKeyspace() {
        session.execute("DROP KEYSPACE ${Database.KEYSPACE};")
    }

    private fun createTable() {
        session.execute(
            """
                CREATE TABLE IF NOT EXISTS  ${Database.KEYSPACE}.${Database.Period.TABLE} (
                     ${Database.Period.CPID}            TEXT,
                     ${Database.Period.OCID}            TEXT,
                     ${Database.Period.OWNER}           TEXT,
                     ${Database.Period.START_DATE}      TIMESTAMP,
                     ${Database.Period.END_DATE}        TIMESTAMP,
                     PRIMARY KEY(${Database.Period.CPID}, ${Database.Period.OCID})
                    );
            """
        )
    }
}