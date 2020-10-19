package com.procurement.clarification.application.model.dto.params

import com.procurement.clarification.application.model.parseCpid
import com.procurement.clarification.application.model.parseDate
import com.procurement.clarification.application.model.parseOcid
import com.procurement.clarification.application.model.parseOperationType
import com.procurement.clarification.application.model.parseOwner
import com.procurement.clarification.application.model.parsePmd
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.Owner
import com.procurement.clarification.domain.model.enums.OperationType
import com.procurement.clarification.domain.model.enums.ProcurementMethod
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asSuccess
import java.time.LocalDateTime

class CreateEnquiryPeriodParams private constructor(
    val cpid: Cpid,
    val ocid: Ocid,
    val tender: Tender,
    val owner: Owner,
    val pmd: ProcurementMethod,
    val country: String,
    val operationType: OperationType
) {
    companion object {

        val allowedPmd = ProcurementMethod.allowedElements
            .filter { value ->
                when (value) {
                    ProcurementMethod.CF, ProcurementMethod.TEST_CF,
                    ProcurementMethod.OF, ProcurementMethod.TEST_OF -> true

                    ProcurementMethod.CD, ProcurementMethod.TEST_CD,
                    ProcurementMethod.DA, ProcurementMethod.TEST_DA,
                    ProcurementMethod.DC, ProcurementMethod.TEST_DC,
                    ProcurementMethod.DCO, ProcurementMethod.TEST_DCO,
                    ProcurementMethod.FA, ProcurementMethod.TEST_FA,
                    ProcurementMethod.GPA, ProcurementMethod.TEST_GPA,
                    ProcurementMethod.IP, ProcurementMethod.TEST_IP,
                    ProcurementMethod.MC, ProcurementMethod.TEST_MC,
                    ProcurementMethod.MV, ProcurementMethod.TEST_MV,
                    ProcurementMethod.NP, ProcurementMethod.TEST_NP,
                    ProcurementMethod.OP, ProcurementMethod.TEST_OP,
                    ProcurementMethod.OT, ProcurementMethod.TEST_OT,
                    ProcurementMethod.RFQ, ProcurementMethod.TEST_RFQ,
                    ProcurementMethod.RT, ProcurementMethod.TEST_RT,
                    ProcurementMethod.SV, ProcurementMethod.TEST_SV -> false
                }
            }.toSet()

        val allowedOperationType = OperationType.allowedElements
            .filter { value ->
                when (value) {
                    OperationType.CREATE_PCR -> true
                }
            }.toSet()

        fun tryCreate(
            cpid: String,
            ocid: String,
            tender: Tender,
            owner: String,
            pmd: String,
            country: String,
            operationType: String
        ): Result<CreateEnquiryPeriodParams, DataErrors> {
            val parsedCpid = parseCpid(value = cpid)
                .orForwardFail { fail -> return fail }

            val parsedOcid = parseOcid(value = ocid)
                .orForwardFail { fail -> return fail }

            val parsedPmd = parsePmd(pmd, allowedPmd)
                .orForwardFail { fail -> return fail }

            val parsedOwner = parseOwner(owner)
                .orForwardFail { fail -> return fail }

            val parsedOperationType = parseOperationType(operationType, allowedOperationType)
                .orForwardFail { fail -> return fail }

            return CreateEnquiryPeriodParams(
                cpid = parsedCpid,
                ocid = parsedOcid,
                owner = parsedOwner,
                pmd = parsedPmd,
                operationType = parsedOperationType,
                country = country,
                tender = tender
            )
                .asSuccess()
        }
    }

    data class Tender(
        val tenderPeriod: TenderPeriod
    ) {
        data class TenderPeriod(
            val startDate: LocalDateTime,
            val endDate: LocalDateTime
        ) {
            companion object {
                fun tryCreate(
                    startDate: String,
                    endDate: String
                ): Result<TenderPeriod, DataErrors> {
                    val startDateParsed = parseDate(startDate, "startDate")
                        .orForwardFail { fail -> return fail }

                    val endDateParsed = parseDate(endDate, "endDate")
                        .orForwardFail { fail -> return fail }

                    return TenderPeriod(startDate = startDateParsed, endDate = endDateParsed).asSuccess()
                }
            }
        }
    }
}

