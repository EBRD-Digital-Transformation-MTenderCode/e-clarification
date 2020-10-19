package com.procurement.clarification.application.service

import com.procurement.clarification.application.model.dto.params.FindEnquiriesParams
import com.procurement.clarification.application.model.dto.params.FindEnquiryIdsParams
import com.procurement.clarification.application.model.dto.params.GetEnquiryByIdsParams
import com.procurement.clarification.application.respository.EnquiryRepository
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.fail.error.ValidationErrors
import com.procurement.clarification.domain.model.enquiry.EnquiryId
import com.procurement.clarification.domain.model.enums.Scale
import com.procurement.clarification.domain.model.lot.LotId
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asFailure
import com.procurement.clarification.domain.util.asSuccess
import com.procurement.clarification.infrastructure.handler.enquiry.find.FindEnquiriesResult
import com.procurement.clarification.infrastructure.handler.enquiry.get.GetEnquiryByIdsResult
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.utils.tryToObject
import org.springframework.stereotype.Service

interface EnquiryService {

    fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail>

    fun getEnquiryByIds(params: GetEnquiryByIdsParams): Result<List<GetEnquiryByIdsResult>, Fail>

    fun findEnquiries(params: FindEnquiriesParams): Result<List<FindEnquiriesResult>, Fail>
}

@Service
class EnquiryServiceImpl(val enquiryRepository: EnquiryRepository) : EnquiryService {

    override fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail> {
        val enquiryEntities = enquiryRepository.findAllByCpidAndStage(cpid = params.cpid, stage = params.ocid.stage)
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

    override fun getEnquiryByIds(params: GetEnquiryByIdsParams): Result<List<GetEnquiryByIdsResult>, Fail> {

        val enquiryEntities = enquiryRepository.findAllByCpidAndStage(cpid = params.cpid, stage = params.ocid.stage)
            .orForwardFail { fail -> return fail }

        if (enquiryEntities.isEmpty())
            return ValidationErrors.EnquiriesNotFoundOnGetEnquiryByIds(cpid = params.cpid, ocid = params.ocid)
                .asFailure()

        val enquiries = enquiryEntities.map { entity ->
            entity.jsonData
                .tryToObject(Enquiry::class.java)
                .doReturn { fail ->
                    return Fail.Incident.DatabaseIncident(exception = fail.exception)
                        .asFailure()
                }
        }
            .associateBy { it.id }

        return params.enquiryIds
            .map { id ->
                enquiries[id]
                    ?.convertToGetEnquiryByIdsResult()
                    ?: return ValidationErrors.EnquiriesNotFoundByIdOnGetEnquiryByIds(id = id)
                        .asFailure()
            }
            .asSuccess()
    }

    override fun findEnquiries(params: FindEnquiriesParams): Result<List<FindEnquiriesResult>, Fail> {

        val isAnswered = params.isAnswer

        val enquiryEntities = enquiryRepository.findAllByCpidAndStage(cpid = params.cpid, stage = params.ocid.stage)
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

    private fun Enquiry.convertToGetEnquiryByIdsResult() =
        GetEnquiryByIdsResult(
            id = this.id!!,
            date = this.date!!,
            answer = this.answer,
            dateAnswer = this.dateAnswered,
            description = this.description,
            title = this.title,
            relatedLot = this.relatedLot
                ?.let { LotId.fromString(it) },
            author = this.author
                .let { organizationReference ->
                    GetEnquiryByIdsResult.Author(
                        id = organizationReference.id!!,
                        name = organizationReference.name,
                        additionalIdentifiers = organizationReference.additionalIdentifiers
                            ?.map { identifier ->
                                GetEnquiryByIdsResult.Author.AdditionalIdentifier(
                                    id = identifier.id,
                                    legalName = identifier.legalName,
                                    scheme = identifier.scheme,
                                    uri = identifier.uri
                                )
                            },
                        contactPoint = organizationReference.contactPoint
                            .let { contactPoint ->
                                GetEnquiryByIdsResult.Author.ContactPoint(
                                    name = contactPoint.name,
                                    email = contactPoint.email,
                                    faxNumber = contactPoint.faxNumber,
                                    telephone = contactPoint.telephone,
                                    url = contactPoint.url
                                )
                            },
                        details = organizationReference.details
                            .let { details ->
                                GetEnquiryByIdsResult.Author.Details(
                                    scale = Scale.creator(name = details!!.scale)
                                )
                            },
                        identifier = organizationReference.identifier
                            .let { identifier ->
                                GetEnquiryByIdsResult.Author.Identifier(
                                    id = identifier.id,
                                    scheme = identifier.scheme,
                                    legalName = identifier.legalName,
                                    uri = identifier.uri
                                )
                            },
                        address = organizationReference.address
                            .let { address ->
                                GetEnquiryByIdsResult.Author.Address(
                                    streetAddress = address.streetAddress,
                                    postalCode = address.postalCode,
                                    addressDetails = address.addressDetails
                                        .let { addressDetails ->
                                            GetEnquiryByIdsResult.Author.Address.AddressDetails(
                                                country = addressDetails.country
                                                    .let { country ->
                                                        GetEnquiryByIdsResult.Author.Address.AddressDetails.Country(
                                                            id = country.id,
                                                            uri = country.uri!!,
                                                            scheme = country.scheme!!,
                                                            description = country.description!!
                                                        )
                                                    },
                                                locality = addressDetails.locality
                                                    .let { locality ->
                                                        GetEnquiryByIdsResult.Author.Address.AddressDetails.Locality(
                                                            id = locality.id,
                                                            uri = locality.uri,
                                                            scheme = locality.scheme,
                                                            description = locality.description
                                                        )
                                                    },
                                                region = addressDetails.region
                                                    .let { region ->
                                                        GetEnquiryByIdsResult.Author.Address.AddressDetails.Region(
                                                            id = region.id,
                                                            uri = region.uri!!,
                                                            scheme = region.scheme!!,
                                                            description = region.description!!
                                                        )
                                                    }
                                            )
                                        }
                                )
                            }
                    )
                }
        )
}

