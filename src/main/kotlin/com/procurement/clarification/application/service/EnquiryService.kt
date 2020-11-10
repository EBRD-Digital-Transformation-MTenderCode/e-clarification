package com.procurement.clarification.application.service

import com.procurement.clarification.application.model.dto.params.FindEnquiriesParams
import com.procurement.clarification.application.model.dto.params.FindEnquiryIdsParams
import com.procurement.clarification.application.repository.enquiry.EnquiryRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.enquiry.EnquiryId
import com.procurement.clarification.domain.model.enums.Scale
import com.procurement.clarification.domain.model.lot.LotId
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asFailure
import com.procurement.clarification.lib.functional.asSuccess
import com.procurement.clarification.infrastructure.handler.enquiry.find.FindEnquiriesResult
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.utils.tryToObject
import org.springframework.stereotype.Service

interface EnquiryService {

    fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail>

    fun findEnquiries(params: FindEnquiriesParams): Result<List<FindEnquiriesResult>, Fail>
}

@Service
class EnquiryServiceImpl(val enquiryRepository: EnquiryRepository) : EnquiryService {

    override fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail> {
        val enquiryEntities = enquiryRepository.findBy(params.cpid, params.ocid)
            .orForwardFail { fail -> return fail }

        if (enquiryEntities.isEmpty())
            return emptyList<EnquiryId>().asSuccess()

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

    override fun findEnquiries(params: FindEnquiriesParams): Result<List<FindEnquiriesResult>, Fail> {

        val isAnswered = params.isAnswer

        val enquiryEntities = enquiryRepository.findBy( params.cpid, params.ocid)
            .orForwardFail { fail -> return fail }

        val filteredEnquiries = if (isAnswered != null) {
            enquiryEntities.filter { it.isAnswered == isAnswered }
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
                    .convertToFindEnquiriesResult()
            }
            .asSuccess()
    }

    private fun Enquiry.convertToFindEnquiriesResult() =
        FindEnquiriesResult(
            id = this.id,
            answer = this.answer,
            date = this.date,
            dateAnswered = this.dateAnswered,
            description = this.description,
            relatedLot = this.relatedLot
                ?.let { LotId.fromString(it) },
            title = this.title,
            author = this.author
                .let { organizationReference ->
                    FindEnquiriesResult.Author(
                        name = organizationReference.name,
                        id = organizationReference.id,
                        additionalIdentifiers = organizationReference.additionalIdentifiers
                            ?.map { it ->
                                FindEnquiriesResult.Author.AdditionalIdentifier(
                                    id = it.id,
                                    scheme = it.scheme,
                                    legalName = it.legalName,
                                    uri = it.uri
                                )
                            },
                        address = organizationReference.address
                            .let { address ->
                                FindEnquiriesResult.Author.Address(
                                    postalCode = address.postalCode,
                                    streetAddress = address.streetAddress,
                                    addressDetails = address.addressDetails
                                        .let { addressDetails ->
                                            FindEnquiriesResult.Author.Address.AddressDetails(
                                                country = addressDetails.country
                                                    .let { it ->
                                                        FindEnquiriesResult.Author.Address.AddressDetails.Country(
                                                            id = it.id,
                                                            scheme = it.scheme,
                                                            description = it.description,
                                                            uri = it.uri
                                                        )
                                                    },
                                                locality = addressDetails.locality
                                                    .let { it ->
                                                        FindEnquiriesResult.Author.Address.AddressDetails.Locality(
                                                            id = it.id,
                                                            scheme = it.scheme,
                                                            description = it.description,
                                                            uri = it.uri
                                                        )
                                                    },
                                                region = addressDetails.region
                                                    .let { it ->
                                                        FindEnquiriesResult.Author.Address.AddressDetails.Region(
                                                            id = it.id,
                                                            scheme = it.scheme,
                                                            description = it.description,
                                                            uri = it.uri
                                                        )
                                                    }
                                            )
                                        }
                                )
                            },
                        contactPoint = organizationReference.contactPoint
                            .let { it ->
                                FindEnquiriesResult.Author.ContactPoint(
                                    name = it.name,
                                    email = it.email,
                                    faxNumber = it.faxNumber,
                                    telephone = it.telephone,
                                    url = it.url
                                )
                            },
                        details = organizationReference.details
                            .let {
                                FindEnquiriesResult.Author.Details(
                                    scale = Scale.creator(name = it!!.scale)
                                )
                            },
                        identifier = organizationReference.identifier
                            .let { it ->
                                FindEnquiriesResult.Author.Identifier(
                                    id = it.id,
                                    legalName = it.legalName,
                                    scheme = it.scheme,
                                    uri = it.uri
                                )
                            }
                    )
                }
        )
}

