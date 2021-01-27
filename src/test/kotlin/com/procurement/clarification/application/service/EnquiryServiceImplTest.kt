package com.procurement.clarification.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.clarification.application.repository.enquiry.EnquiryRepository
import com.procurement.clarification.application.repository.enquiry.model.EnquiryEntity
import com.procurement.clarification.application.repository.period.model.PeriodEntity
import com.procurement.clarification.domain.extension.format
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.enums.Stage
import com.procurement.clarification.domain.model.token.Token
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.infrastructure.api.ApiVersion
import com.procurement.clarification.infrastructure.api.v1.CommandMessage
import com.procurement.clarification.infrastructure.api.v1.CommandTypeV1
import com.procurement.clarification.infrastructure.api.v1.Context
import com.procurement.clarification.infrastructure.bind.api.CommandId
import com.procurement.clarification.infrastructure.bind.configuration
import com.procurement.clarification.infrastructure.handler.v1.model.response.CheckAnswerRs
import com.procurement.clarification.infrastructure.service.JacksonJsonTransform
import com.procurement.clarification.json.loadJson
import com.procurement.clarification.lib.functional.asSuccess
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.*

internal class EnquiryServiceImplTest {

    lateinit var generationService: GenerationService
    lateinit var enquiryRepository: EnquiryRepository
    lateinit var periodService: PeriodService
    lateinit var enquiryService: EnquiryService
    lateinit var transform: Transform

    companion object {
        private val CPID = Cpid.tryCreateOrNull("ocds-t1s2t3-MD-1565251033096")!!
        private val OCID = Ocid.tryCreateOrNull("ocds-b3wdp1-MD-1581509539187-EV-1581509653044")!!
        private val TOKEN = UUID.randomUUID()

        private const val FORMAT_PATTERN = "uuuu-MM-dd'T'HH:mm:ss'Z'"
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN)
            .withResolverStyle(ResolverStyle.STRICT)
        private val DATE = LocalDateTime.parse("2019-02-10T08:49:55Z", FORMATTER)
    }

    @Nested
    inner class CheckEnquiries {

        @BeforeEach
        fun init() {
            generationService = mock()
            enquiryRepository = mock()
            periodService = mock()
            enquiryService = EnquiryServiceImpl(generationService, enquiryRepository, periodService)

            val objectMapper = ObjectMapper().apply { configuration() }
            transform = JacksonJsonTransform(objectMapper)
        }

        @Test
        fun wrongId_exception() {
            val cm = getCommandMessage(id = "wrongId")
            val enquiries = listOf(getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")))
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            val actualException = assertThrows<ErrorException> {
                enquiryService.checkAnswer(cm)
            }
            val expectedCode = "00.01"
            assertEquals(expectedCode, actualException.code)
        }

        @Test
        fun wrongToken_exception() {
            val cm = getCommandMessage(id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3", token = UUID.randomUUID())
            val enquiries = listOf(getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")))
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            val actualException = assertThrows<ErrorException> {
                enquiryService.checkAnswer(cm)
            }
            val expectedCode = "20.06"
            assertEquals(expectedCode, actualException.code)
        }

        @Test
        fun alreadyAnswered_exception() {
            val cm = getCommandMessage(id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3")
            val enquiries = listOf(getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json"), isAnswered = true))
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            val actualException = assertThrows<ErrorException> {
                enquiryService.checkAnswer(cm)
            }
            val expectedCode = "02.01"
            assertEquals(expectedCode, actualException.code)
        }

        @Test
        fun stageTPEndDatePrecedes_exception() {
            val cm = getCommandMessage(id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3", data = loadJson("json/service/answer/check/request_pre_qualification.json"))
            val enquiries = listOf(getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")))
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            val actual = enquiryService.checkAnswer(cm).data as CheckAnswerRs

            assertEquals(actual.setUnsuspended, false)
        }

        @Test
        fun stageFEEndDatePrecedes_exception() {
            val cm = getCommandMessage(
                id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3",
                data = loadJson("json/service/answer/check/request_pre_qualification.json"),
                stage = Stage.FE
            )
            val enquiries = listOf(getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")))
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            val actual = enquiryService.checkAnswer(cm).data as CheckAnswerRs

            assertEquals(actual.setUnsuspended, false)
        }

        @Test
        fun stagePCEndDatePrecedes_exception() {
            val cm = getCommandMessage(
                id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3",
                data = loadJson("json/service/answer/check/request_tender_period.json"),
                stage = Stage.PC
            )
            val enquiries = listOf(getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")))
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            val actual = enquiryService.checkAnswer(cm).data as CheckAnswerRs

            assertEquals(actual.setUnsuspended, false)
        }

        @Test
        fun stageEVEndDatePrecedes_exception() {
            val cm = getCommandMessage(
                id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3",
                data = loadJson("json/service/answer/check/request_tender_period.json"),
                stage = Stage.EV
            )
            val enquiries = listOf(getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")))
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            whenever(periodService.getPeriodEntity(CPID, OCID)).thenReturn(PeriodEntity(CPID, OCID, "", DATE, DATE.plusDays(2)))

            val actual = enquiryService.checkAnswer(cm).data as CheckAnswerRs

            assertEquals(actual.setUnsuspended, false)
        }

        @Test
        fun otherEnquiriesNotAnswered_exception() {
            val cm = getCommandMessage(
                id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3",
                data = loadJson("json/service/answer/check/request_tender_period.json"),
                stage = Stage.EV
            )
            val enquiries = listOf(
                getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")),
                getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data_1.json"), isAnswered = false)
                                      )
            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            whenever(periodService.getPeriodEntity(CPID, OCID)).thenReturn(PeriodEntity(CPID, OCID, "", DATE, DATE.minusDays(2)))

            val actual = enquiryService.checkAnswer(cm).data as CheckAnswerRs

            assertEquals(actual.setUnsuspended, false)
        }

        @Test
        fun otherEnquiriesAnswered_exception() {
            val cm = getCommandMessage(
                id = "a63155e0-0320-11eb-b8f6-2d3c13365ab3",
                data = loadJson("json/service/answer/check/request_tender_period.json"),
                stage = Stage.EV
            )
            val enquiries = listOf(
                getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data.json")),
                getEnquiry(jsonData = loadJson("json/service/answer/check/enquiry_json_data_1.json"), isAnswered = true)
            )

            whenever(enquiryRepository.findBy(CPID, OCID)).thenReturn(enquiries.asSuccess())
            whenever(periodService.getPeriodEntity(CPID, OCID)).thenReturn(PeriodEntity(CPID, OCID, "", DATE, DATE.minusDays(2)))

            val actual = enquiryService.checkAnswer(cm).data as CheckAnswerRs

            assertEquals(true, actual.setUnsuspended)
        }

        private fun getCommandMessage(
            id: String,
            data: String = "{}",
            stage: Stage = Stage.TP,
            token: Token = TOKEN
        ) = CommandMessage(
            id = CommandId.invoke("id"),
            command = CommandTypeV1.CHECK_ANSWER,
            context = Context(
                cpid = CPID.underlying,
                ocid = OCID.underlying,
                token = token.toString(),
                startDate = DATE.format(),
                id = id,
                stage = stage.key,
                owner = null,
                country = null,
                endDate = null,
                access = null,
                language = null,
                operationId = null,
                operationType = null,
                phase = null,
                pmd = null,
                prevStage = null,
                processType = null,
                requestId = null,
                setExtendedPeriod = null,
            ),
            version = ApiVersion(0, 0, 0),
            data = transform.tryParse(data).get
        )

        private fun getEnquiry(
            token: Token = TOKEN,
            isAnswered: Boolean = false,
            jsonData: String
        ) = EnquiryEntity(
            cpid = CPID,
            ocid = OCID,
            token = token,
            isAnswered = isAnswered,
            jsonData = jsonData
        )
    }
}