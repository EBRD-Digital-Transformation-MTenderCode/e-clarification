package com.procurement.clarification.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.model.entity.EnquiryEntity
import org.springframework.stereotype.Service
import java.util.*

interface EnquiryDao {

    fun save(entity: EnquiryEntity)

    fun getByCpIdAndStageAndToken(cpId: String, stage: String, token: UUID): EnquiryEntity

    fun getCountOfUnanswered(cpId: String, stage: String): Long

}

@Service
class EnquiryDaoImpl(private val session: Session) : EnquiryDao {

    override fun save(entity: EnquiryEntity) {
        val insert =
                insertInto(CLARIFICATION_TABLE)
                        .value(CP_ID, entity.cpId)
                        .value(TOKEN, entity.token)
                        .value(STAGE, entity.stage)
                        .value(OWNER, entity.owner)
                        .value(IS_ANSWERED, entity.isAnswered)
                        .value(JSON_DATA, entity.jsonData)
        session.execute(insert)
    }

    override fun getByCpIdAndStageAndToken(cpId: String, stage: String, token: UUID): EnquiryEntity {
        val query = select()
                .all()
                .from(CLARIFICATION_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(STAGE, String))
                .and(eq(TOKEN, token))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null)
            EnquiryEntity(
                    cpId = row.getString(CP_ID),
                    token = row.getUUID(TOKEN),
                    stage = row.getString(STAGE),
                    owner = row.getString(OWNER),
                    isAnswered = row.getBool(IS_ANSWERED),
                    jsonData = row.getString(JSON_DATA))
        else throw ErrorException(ErrorType.DATA_NOT_FOUND)
    }

    override fun getCountOfUnanswered(cpId: String, stage: String): Long {
        val query = select()
                .countAll()
                .from(CLARIFICATION_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(STAGE, stage))
                .and(eq(IS_ANSWERED, false))
                .allowFiltering()
        val row = session.execute(query).one()
        return if (row != null) return row.getLong(0)
        else throw ErrorException(ErrorType.DATA_NOT_FOUND)
    }

    companion object {
        private val CLARIFICATION_TABLE = "clarification_enquiry"
        private val CP_ID = "cp_id"
        private val TOKEN = "token_entity"
        private val STAGE = "stage"
        private val OWNER = "owner"
        private val IS_ANSWERED = "is_answered"
        private val JSON_DATA = "json_data"
    }
}
