package com.procurement.clarification.service

import com.procurement.clarification.application.model.dto.period.create.CreatePeriodContext
import com.procurement.clarification.application.model.dto.period.create.CreatePeriodData
import com.procurement.clarification.dao.HistoryDao
import com.procurement.clarification.infrastructure.model.dto.period.create.request.CreatePeriodRequest
import com.procurement.clarification.infrastructure.model.dto.period.create.request.convert
import com.procurement.clarification.infrastructure.model.dto.period.create.response.CreatePeriodResponse
import com.procurement.clarification.infrastructure.model.dto.period.create.response.convert
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.CommandType
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.model.dto.bpe.country
import com.procurement.clarification.model.dto.bpe.cpid
import com.procurement.clarification.model.dto.bpe.owner
import com.procurement.clarification.model.dto.bpe.pmd
import com.procurement.clarification.model.dto.bpe.stage
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.toObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CommandService(private val historyDao: HistoryDao,
                     private val enquiryService: EnquiryService,
                     private val periodService: PeriodService) {

    companion object {
        private val log = LoggerFactory.getLogger(CommandService::class.java)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        var historyEntity = historyDao.getHistory(cm.id, cm.command.value())
        if (historyEntity != null) {
            return toObject(ResponseDto::class.java, historyEntity.jsonData)
        }
        val response = when (cm.command) {
            CommandType.CREATE_ENQUIRY -> enquiryService.createEnquiry(cm)
            CommandType.ADD_ANSWER -> enquiryService.addAnswer(cm)
            CommandType.CHECK_ANSWER -> enquiryService.checkAnswer(cm)
            CommandType.CHECK_ENQUIRIES -> enquiryService.checkEnquiries(cm)
            CommandType.CHECK_PERIOD -> periodService.checkPeriod(cm)
            CommandType.SAVE_PERIOD -> periodService.savePeriod(cm)
            CommandType.SAVE_NEW_PERIOD -> periodService.saveNewPeriod(cm)
            CommandType.VALIDATE_PERIOD -> periodService.periodValidation(cm)
            CommandType.CALCULATE_SAVE_PERIOD -> periodService.calculateAndSavePeriod(cm)
            CommandType.GET_PERIOD -> periodService.getPeriod(cm)
            CommandType.CREATE_PERIOD -> {
                val context = CreatePeriodContext(
                    owner = cm.owner,
                    stage = cm.stage,
                    cpid = cm.cpid,
                    pmd = cm.pmd,
                    country = cm.country
                )
                val request: CreatePeriodRequest = toObject(CreatePeriodRequest::class.java, cm.data)
                val data: CreatePeriodData = request.convert()
                val result = periodService.createPeriod(context, data)
                if (log.isDebugEnabled)
                    log.debug("Update CN. Result: ${toJson(result)}")

                val response: CreatePeriodResponse = result.convert()
                if (log.isDebugEnabled)
                    log.debug("Update CN. Response: ${toJson(response)}")

                return ResponseDto(data = response)
            }
        }
        historyEntity = historyDao.saveHistory(cm.id, cm.command.value(), response)
        return toObject(ResponseDto::class.java, historyEntity.jsonData)
    }
}