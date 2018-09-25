package com.procurement.clarification.service

import com.procurement.clarification.dao.HistoryDao
import com.procurement.clarification.model.dto.bpe.CommandMessage
import com.procurement.clarification.model.dto.bpe.CommandType
import com.procurement.clarification.model.dto.bpe.ResponseDto
import com.procurement.clarification.utils.toObject
import org.springframework.stereotype.Service

interface CommandService {

    fun execute(cm: CommandMessage): ResponseDto

}

@Service
class CommandServiceImpl(private val historyDao: HistoryDao,
                         private val enquiryService: EnquiryService,
                         private val periodService: PeriodService) : CommandService {


    override fun execute(cm: CommandMessage): ResponseDto {
        var historyEntity = historyDao.getHistory(cm.context.operationId, cm.command.value())
        if (historyEntity != null) {
            return toObject(ResponseDto::class.java, historyEntity.jsonData)
        }
        val response = when (cm.command) {
            CommandType.CREATE_ENQUIRY -> enquiryService.createEnquiry(cm)
            CommandType.ADD_ANSWER -> enquiryService.addAnswer(cm)
            CommandType.REMOVE_ANSWER -> enquiryService.removeAnswer(cm)
            CommandType.CHECK_ENQUIRIES -> enquiryService.checkEnquiries(cm)
            CommandType.CHECK_PERIOD -> periodService.checkPeriod(cm)
            CommandType.SAVE_PERIOD -> periodService.savePeriod(cm)
            CommandType.SAVE_NEW_PERIOD -> periodService.saveNewPeriod(cm)
            CommandType.VALIDATE_PERIOD -> periodService.periodValidation(cm)
            CommandType.CALCULATE_SAVE_PERIOD -> periodService.calculateAndSavePeriod(cm)
            CommandType.GET_PERIOD -> periodService.getPeriod(cm)
        }
        historyEntity = historyDao.saveHistory(cm.context.operationId, cm.command.value(), response)
        return toObject(ResponseDto::class.java, historyEntity.jsonData)
    }
}