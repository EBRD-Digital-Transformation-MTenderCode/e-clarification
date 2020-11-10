package com.procurement.clarification.infrastructure.service

import com.procurement.clarification.application.model.dto.period.create.CreatePeriodContext
import com.procurement.clarification.application.model.dto.period.create.CreatePeriodData
import com.procurement.clarification.application.service.EnquiryService
import com.procurement.clarification.domain.extension.nowDefaultUTC
import com.procurement.clarification.infrastructure.handler.HistoryRepository
import com.procurement.clarification.infrastructure.handler.v1.model.request.CreatePeriodRequest
import com.procurement.clarification.infrastructure.handler.v1.converter.convert
import com.procurement.clarification.infrastructure.handler.v1.model.response.CreatePeriodResponse
import com.procurement.clarification.infrastructure.repository.history.model.HistoryEntity
import com.procurement.clarification.infrastructure.api.v1.CommandMessage
import com.procurement.clarification.infrastructure.api.v1.CommandTypeV1
import com.procurement.clarification.infrastructure.api.v1.ResponseDto
import com.procurement.clarification.infrastructure.api.v1.action
import com.procurement.clarification.infrastructure.api.v1.commandId
import com.procurement.clarification.infrastructure.api.v1.country
import com.procurement.clarification.infrastructure.api.v1.cpid
import com.procurement.clarification.infrastructure.api.v1.ocid
import com.procurement.clarification.infrastructure.api.v1.owner
import com.procurement.clarification.infrastructure.api.v1.pmd
import com.procurement.clarification.application.service.PeriodService
import com.procurement.clarification.infrastructure.api.v1.converter.convert
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.toObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CommandServiceV1(
    private val historyRepository: HistoryRepository,
    private val enquiryService: EnquiryService,
    private val periodService: PeriodService
) {

    companion object {
        private val log = LoggerFactory.getLogger(CommandServiceV1::class.java)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        val history = historyRepository.getHistory(cm.commandId, cm.action)
            .onFailure {
                throw RuntimeException("Error of loading history. ${it.reason.description}", it.reason.exception)
            }
        if (history != null) {
            return toObject(ResponseDto::class.java, history)
        }
        val response = when (cm.command) {
            CommandTypeV1.ADD_ANSWER -> enquiryService.addAnswer(cm)
            CommandTypeV1.CHECK_ANSWER -> enquiryService.checkAnswer(cm)
            CommandTypeV1.CHECK_ENQUIRIES -> enquiryService.checkEnquiries(cm)
            CommandTypeV1.CHECK_PERIOD -> periodService.checkPeriod(cm)
            CommandTypeV1.CREATE_ENQUIRY -> enquiryService.createEnquiry(cm)
            CommandTypeV1.CREATE_PERIOD -> {
                val context = CreatePeriodContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    owner = cm.owner,
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
            CommandTypeV1.SAVE_NEW_PERIOD -> periodService.saveNewPeriod(cm)
            CommandTypeV1.SAVE_PERIOD -> periodService.savePeriod(cm)
            CommandTypeV1.VALIDATE_PERIOD -> periodService.periodValidation(cm)
        }
        val historyEntity = HistoryEntity(
            commandId = cm.commandId,
            action = cm.action,
            date = nowDefaultUTC(),
            data = toJson(response)
        )
        historyRepository.saveHistory(historyEntity)
            .doOnError {
                log.error("Error of save history. ${it.description}", it.exception)
            }
        return toObject(ResponseDto::class.java, historyEntity.data)
    }
}