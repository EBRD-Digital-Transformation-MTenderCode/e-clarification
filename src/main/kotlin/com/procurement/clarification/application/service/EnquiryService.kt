package com.procurement.clarification.application.service

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
import com.procurement.clarification.infrastructure.handler.get.enquirybyids.GetEnquiryByIdsResult
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.utils.tryToObject
import org.springframework.stereotype.Service

interface EnquiryService {

    fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail>

    fun getEnquiryByIds(params: GetEnquiryByIdsParams): Result<List<GetEnquiryByIdsResult>, Fail>
}

@Service
class EnquiryServiceImpl(val enquiryRepository: EnquiryRepository) : EnquiryService {

    override fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail> {
        val enquiryEntities = enquiryRepository.findAllByCpidAndStage(cpid = params.cpid, stage = params.ocid.stage)
            .orForwardFail { fail -> return fail }

        if (enquiryEntities.isEmpty())
            return ValidationErrors.EnquiriesNotFoundOnFindEnquiryIds(cpid = params.cpid, ocid = params.ocid)
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

    private fun Enquiry.convertToGetEnquiryByIdsResult() =
        GetEnquiryByIdsResult(
            id = this.id!!,
            date = this.date!!,
            answer = this.answer,
            dateAnswer = this.dateAnswered,
            description = this.description,
            title = this.title,
            relatedLot = if (this.relatedLot != null) LotId.fromString(this.relatedLot) else null,
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
                                GetEnquiryByIdsResult.Author.Details(scale = Scale.creator(name = details!!.scale))
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

