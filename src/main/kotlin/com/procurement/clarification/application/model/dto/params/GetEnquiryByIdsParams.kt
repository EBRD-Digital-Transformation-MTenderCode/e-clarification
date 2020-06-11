package com.procurement.clarification.application.model.dto.params

import com.procurement.clarification.application.model.parseCpid
import com.procurement.clarification.application.model.parseOcid
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.enquiry.EnquiryId
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asSuccess

class GetEnquiryByIdsParams private constructor(
    val cpid: Cpid,
    val ocid: Ocid,
    val enquiryIds: List<EnquiryId>
) {
    companion object {
        fun tryCreate(
            cpid: String,
            ocid: String,
            enquiryIds: List<String>
        ): Result<GetEnquiryByIdsParams, DataErrors> {

            val cpidParsed = parseCpid(value = cpid)
                .orForwardFail { fail -> return fail }

            val ocidParsed = parseOcid(value = ocid)
                .orForwardFail { fail -> return fail }

            return GetEnquiryByIdsParams(cpid = cpidParsed, ocid = ocidParsed, enquiryIds = enquiryIds)
                .asSuccess()
        }
    }
}
