package com.procurement.clarification.dao

import com.datastax.driver.core.Row
import com.datastax.driver.core.Session
import com.procurement.clarification.application.respository.EnquiryRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.enums.Stage
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asSuccess
import com.procurement.clarification.infrastructure.extension.tryExecute
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

    }

    private val preparedFindAllByCpidAndStage = session.prepare(FIND_ALL_BY_CPID_AND_STAGE_CQL)

    override fun findAllByCpidAndStage(cpid: Cpid, stage: Stage): Result<List<EnquiryEntity>, Fail.Incident.Database> {
        val query = preparedFindAllByCpidAndStage.bind()
            .apply {
                setString(COLUMN_CP_ID, cpid.toString())
                setString(COLUMN_STAGE, stage.toString())
            }
        return query.tryExecute(session)
            .orForwardFail { fail -> return fail }
            .map { row -> row.extractEnquiryEntity() }
            .asSuccess()
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
