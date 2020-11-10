package com.procurement.clarification.infrastructure.repository.enquiry

import com.datastax.driver.core.Session
import com.procurement.clarification.application.repository.enquiry.EnquiryRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.token.Token
import com.procurement.clarification.infrastructure.extension.cassandra.tryExecute
import com.procurement.clarification.infrastructure.repository.Database
import com.procurement.clarification.application.repository.enquiry.model.EnquiryEntity
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asSuccess
import org.springframework.stereotype.Repository

@Repository
class CassandraEnquiryRepository(private val session: Session) : EnquiryRepository {

    companion object {

        private const val FIND_BY_CPID_AND_OCID_CQL = """
            SELECT ${Database.Enquiry.TOKEN_ENTITY},
                   ${Database.Enquiry.IS_ANSWERED},
                   ${Database.Enquiry.JSON_DATA}
              FROM ${Database.KEYSPACE}.${Database.Enquiry.TABLE}
             WHERE ${Database.Enquiry.CPID}=?
               AND ${Database.Enquiry.OCID}=?
        """

        private const val FIND_BY_CPID_AND_OCID_AND_TOKEN_CQL = """
            SELECT ${Database.Enquiry.IS_ANSWERED},
                   ${Database.Enquiry.JSON_DATA}
              FROM ${Database.KEYSPACE}.${Database.Enquiry.TABLE}
             WHERE ${Database.Enquiry.CPID}=?
               AND ${Database.Enquiry.OCID}=?
               AND ${Database.Enquiry.TOKEN_ENTITY}=?
        """

        private const val NEW_ENQUIRY_CQL = """
            INSERT INTO ${Database.KEYSPACE}.${Database.Enquiry.TABLE}
            (
                ${Database.Enquiry.CPID},
                ${Database.Enquiry.OCID},
                ${Database.Enquiry.TOKEN_ENTITY},
                ${Database.Enquiry.IS_ANSWERED},
                ${Database.Enquiry.JSON_DATA}
            )
            VALUES(?, ?, ?, ?, ?)
        """
    }

    private val preparedFindByCpidAndOcid = session.prepare(FIND_BY_CPID_AND_OCID_CQL)
    private val preparedFindByCpidAndOcidAndToken = session.prepare(FIND_BY_CPID_AND_OCID_AND_TOKEN_CQL)
    private val preparedNewEnquiry = session.prepare(NEW_ENQUIRY_CQL)

    override fun findBy(cpid: Cpid, ocid: Ocid): Result<List<EnquiryEntity>, Fail.Incident.Database> {
        val query = preparedFindByCpidAndOcid.bind()
            .apply {
                setString(Database.Enquiry.CPID, cpid.underlying)
                setString(Database.Enquiry.OCID, ocid.underlying)
            }
        return query.tryExecute(session)
            .onFailure { return it }
            .map { row ->
                EnquiryEntity(
                    cpid = cpid,
                    ocid = ocid,
                    token = Token.fromString(row.getString(Database.Enquiry.TOKEN_ENTITY)),
                    isAnswered = row.getBool(Database.Enquiry.IS_ANSWERED),
                    jsonData = row.getString(Database.Enquiry.JSON_DATA)
                )
            }
            .asSuccess()
    }

    override fun findBy(cpid: Cpid, ocid: Ocid, token: Token): Result<EnquiryEntity?, Fail.Incident.Database> {
        val query = preparedFindByCpidAndOcidAndToken.bind()
            .apply {
                setString(Database.Enquiry.CPID, cpid.underlying)
                setString(Database.Enquiry.OCID, ocid.underlying)
                setString(Database.Enquiry.TOKEN_ENTITY, token.toString())
            }
        return query.tryExecute(session)
            .onFailure { return it }
            .one()
            ?.let { row ->
                EnquiryEntity(
                    cpid = cpid,
                    ocid = ocid,
                    token = token,
                    isAnswered = row.getBool(Database.Enquiry.IS_ANSWERED),
                    jsonData = row.getString(Database.Enquiry.JSON_DATA)
                )
            }
            .asSuccess()
    }

    override fun save(entity: EnquiryEntity): Result<Boolean, Fail.Incident.Database> {
        val query = preparedNewEnquiry.bind()
            .apply {
                setString(Database.Enquiry.CPID, entity.cpid.underlying)
                setString(Database.Enquiry.OCID, entity.ocid.underlying)
                setString(Database.Enquiry.TOKEN_ENTITY, entity.token.toString())
                setBool(Database.Enquiry.IS_ANSWERED, entity.isAnswered)
                setString(Database.Enquiry.JSON_DATA, entity.jsonData)
            }
        return query.tryExecute(session)
            .onFailure { return it }
            .wasApplied()
            .asSuccess()
    }
}
