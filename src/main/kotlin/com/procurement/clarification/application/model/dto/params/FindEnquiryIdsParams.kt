package com.procurement.clarification.application.model.dto.params

import com.procurement.clarification.application.model.parseCpid
import com.procurement.clarification.application.model.parseOcid
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asSuccess

class FindEnquiryIdsParams private constructor(
    val cpid: Cpid,
    val ocid: Ocid,
    val isAnswer: Boolean?
) {
    companion object {
        fun tryCreate(
            cpid: String,
            ocid: String,
            isAnswer: Boolean?
        ): Result<FindEnquiryIdsParams, DataErrors> {

            val parsedCpid = parseCpid(value = cpid)
                .orForwardFail { fail -> return fail }

            val parsedOcid = parseOcid(value = ocid)
                .orForwardFail { fail -> return fail }

            return FindEnquiryIdsParams(cpid = parsedCpid, ocid = parsedOcid, isAnswer = isAnswer)
                .asSuccess()
        }
    }
}
