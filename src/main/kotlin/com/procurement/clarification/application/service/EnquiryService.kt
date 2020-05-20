package com.procurement.clarification.application.service

import com.procurement.clarification.application.model.dto.params.FindEnquiryIdsParams
import com.procurement.clarification.application.respository.EnquiryRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.error.ValidationErrors
import com.procurement.clarification.domain.model.enquiry.EnquiryId
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asFailure
import com.procurement.clarification.domain.util.asSuccess
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.utils.tryToObject
import org.springframework.stereotype.Service

interface EnquiryService {

    fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail>
}

@Service
class EnquiryServiceImpl(
    val enquiryRepository: EnquiryRepository
) : EnquiryService {

    override fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail> {
        val enquiryEntities = enquiryRepository.findAllByCpidAndStage(cpid = params.cpid, stage = params.ocid.stage)
            .doReturn { fail -> return Fail.Incident.DatabaseIncident(exception = fail.exception).asFailure() }

        if (enquiryEntities.isEmpty())
            return ValidationErrors.EnquiriesNotFoundOnFindEnquiriesIds(cpid = params.cpid, ocid = params.ocid)
                .asFailure()

        val filteredEnquiries = if (params.isAnswer != null) {
            enquiryEntities.filter { params.isAnswer == it.isAnswered }
        } else {
            enquiryEntities
        }

        return filteredEnquiries
            .map { entity ->
                entity.jsonData
                    .tryToObject(Enquiry::class.java)
                    .doReturn { fail ->
                        return Fail.Incident.DatabaseIncident(exception = fail.exception)
                            .asFailure()
                    }
                    .let { it.id!! }
            }
            .asSuccess()
    }
}

