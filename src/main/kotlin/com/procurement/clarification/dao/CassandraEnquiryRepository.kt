package com.procurement.clarification.dao

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Row
import com.datastax.driver.core.Session
import com.procurement.clarification.application.respository.EnquiryRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.enums.Stage
import com.procurement.clarification.domain.model.token.Token
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.Result.Companion.failure
import com.procurement.clarification.domain.util.Result.Companion.success
import com.procurement.clarification.domain.util.asSuccess
import com.procurement.clarification.model.entity.EnquiryEntity
import org.springframework.stereotype.Repository

@Repository
class CassandraEnquiryRepository(private val session: Session) : EnquiryRepository {

    companion object {
        private const val KEYSPACE = "ocds"
        private const val CLARIFICATION_TABLE = "clarification_enquiry"
        private const val COLUMN_CP_ID = "cp_id"
        private const val COLUMN_TOKEN = "token_entity"
        private const val COLUMN_STAGE = "stage"
        private const val COLUMN_IS_ANSWERED = "is_answered"
        private const val COLUMN_JSON_DATA = "json_data"

        private const val SAVE_CQL = """
            INSERT INTO $KEYSPACE.$CLARIFICATION_TABLE(
            $COLUMN_CP_ID,
            $COLUMN_TOKEN,
            $COLUMN_STAGE,
            $COLUMN_IS_ANSWERED,
            $COLUMN_JSON_DATA
            )
            VALUES (?, ?, ?, ?, ?)
        """
        private const val FIND_ALL_BY_CPID_AND_STAGE_CQL = """
            SELECT $COLUMN_CP_ID,
                   $COLUMN_TOKEN,
                   $COLUMN_STAGE,
                   $COLUMN_IS_ANSWERED,
                   $COLUMN_JSON_DATA
              FROM $KEYSPACE.$CLARIFICATION_TABLE
             WHERE $COLUMN_CP_ID=?
               AND $COLUMN_STAGE=?
        """

        private const val FIND_ALL_BY_CPID_AND_STAGE_AND_TOKEN_CQL = """
            SELECT $COLUMN_CP_ID,
                   $COLUMN_TOKEN,
                   $COLUMN_STAGE,
                   $COLUMN_IS_ANSWERED,
                   $COLUMN_JSON_DATA
              FROM $KEYSPACE.$CLARIFICATION_TABLE
             WHERE $COLUMN_CP_ID=?
               AND $COLUMN_STAGE=?
               AND $COLUMN_TOKEN=?
        """
    }

    private val preparedSaveStatement = session.prepare(SAVE_CQL)
    private val preparedFindAllByCpidAndStage = session.prepare(FIND_ALL_BY_CPID_AND_STAGE_CQL)
    private val preparedFindAllByCpidAndStageAndToken = session.prepare(FIND_ALL_BY_CPID_AND_STAGE_AND_TOKEN_CQL)

    override fun save(entity: EnquiryEntity): Result<Unit, Fail.Incident.Database> {
        val query = preparedSaveStatement.bind()
            .apply {
                setString(COLUMN_CP_ID, entity.cpId)
                setUUID(COLUMN_TOKEN, entity.token)
                setString(COLUMN_STAGE, entity.stage)
                setBool(COLUMN_IS_ANSWERED, entity.isAnswered)
                setString(COLUMN_JSON_DATA, entity.jsonData)
            }
        query.tryExecute()
            .orForwardFail { fail -> return fail }

        return Unit.asSuccess()
    }

    override fun findAllByCpidAndStage(cpid: Cpid, stage: Stage): Result<List<EnquiryEntity>, Fail.Incident.Database> {
        val query = preparedFindAllByCpidAndStage.bind()
            .apply {
                setString(COLUMN_CP_ID, cpid.toString())
                setString(COLUMN_STAGE, stage.toString())
            }
        return query.tryExecute()
            .orForwardFail { fail -> return fail }
            .map { row -> row.extractEnquiryEntity() }
            .asSuccess()
    }

    override fun getByCpidAndStageAndToken(
        cpid: Cpid,
        stage: Stage,
        token: Token
    ): Result<EnquiryEntity?, Fail.Incident.Database> {
        val query = preparedFindAllByCpidAndStageAndToken.bind()
            .apply {
                setString(COLUMN_CP_ID, cpid.toString())
                setString(COLUMN_STAGE, stage.toString())
                setUUID(COLUMN_TOKEN, token)
            }
        return query.tryExecute()
            .orForwardFail { fail -> return fail }
            .one()
            ?.extractEnquiryEntity()
            .asSuccess()
    }

    private fun BoundStatement.tryExecute(): Result<ResultSet, Fail.Incident.Database> = try {
        success(session.execute(this))
    } catch (expected: Exception) {
        failure(Fail.Incident.Database(expected))
    }

    private fun Row.extractEnquiryEntity() =
        EnquiryEntity(
            cpId = getString(COLUMN_CP_ID),
            token = getUUID(COLUMN_TOKEN),
            stage = getString(COLUMN_STAGE),
            isAnswered = getBool(COLUMN_IS_ANSWERED),
            jsonData = getString(COLUMN_JSON_DATA)
        )
}
