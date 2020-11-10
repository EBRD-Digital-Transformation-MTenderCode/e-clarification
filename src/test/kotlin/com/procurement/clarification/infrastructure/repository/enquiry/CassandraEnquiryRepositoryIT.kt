package com.procurement.clarification.infrastructure.repository.enquiry

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.HostDistance
import com.datastax.driver.core.PlainTextAuthProvider
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.Session
import com.nhaarman.mockito_kotlin.spy
import com.procurement.clarification.application.repository.enquiry.EnquiryRepository
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.token.tryCreateToken
import com.procurement.clarification.infrastructure.configuration.DatabaseTestConfiguration
import com.procurement.clarification.infrastructure.repository.CassandraTestContainer
import com.procurement.clarification.infrastructure.repository.Database
import com.procurement.clarification.application.repository.enquiry.model.EnquiryEntity
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
        private val CPID = Cpid.tryCreateOrNull("ocds-t1s2t3-MD-1565251033096")!!
        private val OCID = Ocid.tryCreateOrNull("ocds-b3wdp1-MD-1581509539187-EV-1581509653044")!!
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
    fun findByCpidAndOcid_success() {
        val entity = createEntity()
        val isSaved = enquiryRepository.save(entity).get
        assertTrue(isSaved)

        val dbEnquiryEntity = enquiryRepository.findBy(CPID, OCID).get

        val expectedEntityList = listOf(entity)

        assertFalse(dbEnquiryEntity.isEmpty())
        assertEquals(expectedEntityList, dbEnquiryEntity)
    }

    @Test
    fun findByCpidAndOcid_fail() {
        val dbEnquiryEntity = enquiryRepository.findBy(CPID, OCID).get

        assertTrue(dbEnquiryEntity.isEmpty())
    }

    @Test
    fun findByCpidAndOcidAndToken_success() {
        val entity = createEntity()
        val isSaved = enquiryRepository.save(entity).get
        assertTrue(isSaved)

        val dbEnquiryEntity = enquiryRepository.findBy(CPID, OCID, TOKEN).get

        assertNotNull(dbEnquiryEntity)
        assertEquals(entity, dbEnquiryEntity)
    }

    @Test
    fun findByCpidAndOcidAndToken_fail() {
        val dbEnquiryEntity = enquiryRepository.findBy(CPID, OCID, TOKEN).get

        assertNull(dbEnquiryEntity)
    }

    @Test
    fun save() {
        val entity = createEntity()
        val isSaved = enquiryRepository.save(entity).get
        assertTrue(isSaved)
    }

    private fun createEntity() = EnquiryEntity(
        cpid = CPID,
        ocid = OCID,
        token = TOKEN,
        isAnswered = IS_ANSWERED,
        jsonData = JSON_DATA
    )

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
                CREATE TABLE IF NOT EXISTS  ${Database.KEYSPACE}.${Database.Enquiry.TABLE} (
                     ${Database.Enquiry.CPID}         TEXT,
                     ${Database.Enquiry.OCID}         TEXT,
                     ${Database.Enquiry.TOKEN_ENTITY} TEXT,
                     ${Database.Enquiry.JSON_DATA}    TEXT,
                     ${Database.Enquiry.IS_ANSWERED}  BOOLEAN,
                     PRIMARY KEY(${Database.Enquiry.CPID}, ${Database.Enquiry.OCID}, ${Database.Enquiry.TOKEN_ENTITY})
                    );
            """
        )
    }
}