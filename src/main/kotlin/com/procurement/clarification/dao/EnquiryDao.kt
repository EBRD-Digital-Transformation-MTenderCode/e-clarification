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

    fun findAllByCpIdAndStage(cpId: String, stage: String): List<EnquiryEntity>

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
                        .value(IS_ANSWERED, entity.isAnswered)
                        .value(JSON_DATA, entity.jsonData)
        session.execute(insert)
    }

    override fun findAllByCpIdAndStage(cpId: String, stage: String): List<EnquiryEntity> {
        val query = select().all()
                .from(CLARIFICATION_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(STAGE, stage))
        val resultSet = session.execute(query)
        val entities = ArrayList<EnquiryEntity>()
        resultSet.forEach { row ->
            entities.add(EnquiryEntity(
                    cpId = row.getString(CP_ID),
                    token = row.getUUID(TOKEN),
                    stage = row.getString(STAGE),
                    isAnswered = row.getBool(IS_ANSWERED),
                    jsonData = row.getString(JSON_DATA)))
        }
        return entities
    }

    override fun getByCpIdAndStageAndToken(cpId: String, stage: String, token: UUID): EnquiryEntity {
        val query = select()
                .all()
                .from(CLARIFICATION_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(STAGE, stage))
                .and(eq(TOKEN, token))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null)
            EnquiryEntity(
                    cpId = row.getString(CP_ID),
                    token = row.getUUID(TOKEN),
                    stage = row.getString(STAGE),
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
        if (row != null) return row.getLong(0)
        else throw ErrorException(ErrorType.DATA_NOT_FOUND)
    }

    companion object {
        private const val CLARIFICATION_TABLE = "clarification_enquiry"
        private const val CP_ID = "cp_id"
        private const val TOKEN = "token_entity"
        private const val STAGE = "stage"
        private const val IS_ANSWERED = "is_answered"
        private const val JSON_DATA = "json_data"
    }
}
