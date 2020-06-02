package com.procurement.clarification.infrastructure.repository

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.HostDistance
import com.datastax.driver.core.PlainTextAuthProvider
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.clarification.application.respository.EnquiryRepository
import com.procurement.clarification.dao.CassandraEnquiryRepository
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.enums.Stage
import com.procurement.clarification.domain.model.token.tryCreateToken
import com.procurement.clarification.infrastructure.configuration.DatabaseTestConfiguration
import com.procurement.clarification.model.entity.EnquiryEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DatabaseTestConfiguration::class])
class CassandraEnquiryRepositoryIT {

    companion object {
        private const val KEYSPACE = "ocds"
        private const val TABLE_NAME = "clarification_enquiry"
        private const val COLUMN_CP_ID = "cp_id"
        private const val COLUMN_TOKEN = "token_entity"
        private const val COLUMN_STAGE = "stage"
        private const val COLUMN_IS_ANSWERED = "is_answered"
        private const val COLUMN_JSON_DATA = "json_data"

        private val CPID = Cpid.tryCreateOrNull("ocds-t1s2t3-MD-1565251033096")!!
        private val STAGE = Stage.AC
        private val TOKEN = "5d88e696-6a7d-48e1-b760-ab89aa57cfd8".tryCreateToken().get
        private const val IS_ANSWERED = false
        private const val JSON_DATA = ""
    }

    @Autowired
    private lateinit var container: CassandraTestContainer

    private lateinit var session: Session
    private lateinit var enquiryRepository: EnquiryRepository

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

        enquiryRepository = CassandraEnquiryRepository(session)
    }

    @AfterEach
    fun clean() {
        dropKeyspace()
    }



    @Test
    fun findByCpidAndStage_success() {

        val entity = createEntity()
        insertInto(entity)
        val dbEnquiryEntity = enquiryRepository.findAllByCpidAndStage(cpid = CPID, stage = STAGE).get

        val expectedEntityList = listOf(entity)

        assertFalse(dbEnquiryEntity.isEmpty())
        assertEquals(expectedEntityList, dbEnquiryEntity)
    }

    @Test
    fun findByCpidAndStage_fail() {
        val dbEnquiryEntity = enquiryRepository.findAllByCpidAndStage(cpid = CPID, stage = STAGE).get

        assertTrue(dbEnquiryEntity.isEmpty())
    }

    private fun insertInto(enquiryEntity: EnquiryEntity) {
        val record = QueryBuilder.insertInto(KEYSPACE, TABLE_NAME)
            .value(COLUMN_CP_ID, enquiryEntity.cpId)
            .value(COLUMN_IS_ANSWERED, enquiryEntity.isAnswered)
            .value(COLUMN_JSON_DATA, enquiryEntity.jsonData)
            .value(COLUMN_STAGE, enquiryEntity.stage)
            .value(COLUMN_TOKEN, enquiryEntity.token)

        session.execute(record)
    }

    private fun createEntity() = EnquiryEntity(
        cpId = CPID.toString(),
        stage = STAGE.key,
        token = TOKEN,
        isAnswered = IS_ANSWERED,
        jsonData = JSON_DATA
    )

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
                CREATE TABLE IF NOT EXISTS  $KEYSPACE.$TABLE_NAME (
                     $COLUMN_CP_ID text,
                     $COLUMN_STAGE text,
                     $COLUMN_TOKEN uuid,
                     $COLUMN_JSON_DATA text,
                     $COLUMN_IS_ANSWERED boolean,
                     primary key($COLUMN_CP_ID, $COLUMN_STAGE, $COLUMN_TOKEN)
                    );
            """
        )
    }
}