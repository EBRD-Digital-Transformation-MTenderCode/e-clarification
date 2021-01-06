package com.procurement.clarification.application.service

import com.procurement.clarification.application.model.dto.params.FindEnquiriesParams
import com.procurement.clarification.application.model.dto.params.FindEnquiryIdsParams
import com.procurement.clarification.application.repository.enquiry.EnquiryRepository
import com.procurement.clarification.application.repository.enquiry.model.EnquiryEntity
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.enquiry.EnquiryId
import com.procurement.clarification.domain.model.enums.Scale
import com.procurement.clarification.domain.model.lot.LotId
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import com.procurement.clarification.infrastructure.api.v1.CommandMessage
import com.procurement.clarification.infrastructure.api.v1.ResponseDto
import com.procurement.clarification.infrastructure.api.v1.country
import com.procurement.clarification.infrastructure.api.v1.cpid
import com.procurement.clarification.infrastructure.api.v1.ctxId
import com.procurement.clarification.infrastructure.api.v1.ocid
import com.procurement.clarification.infrastructure.api.v1.owner
import com.procurement.clarification.infrastructure.api.v1.pmd
import com.procurement.clarification.infrastructure.api.v1.startDate
import com.procurement.clarification.infrastructure.api.v1.token
import com.procurement.clarification.infrastructure.handler.v1.model.request.AddAnswerRq
import com.procurement.clarification.infrastructure.handler.v1.model.request.CreateEnquiryRq
import com.procurement.clarification.infrastructure.handler.v1.model.request.OrganizationReferenceCreate
import com.procurement.clarification.infrastructure.handler.v1.model.response.AddAnswerRs
import com.procurement.clarification.infrastructure.handler.v1.model.response.CheckAnswerRs
import com.procurement.clarification.infrastructure.handler.v1.model.response.CheckEnquiresRs
import com.procurement.clarification.infrastructure.handler.v1.model.response.CreateEnquiryRs
import com.procurement.clarification.infrastructure.handler.v2.model.response.FindEnquiriesResult
import com.procurement.clarification.lib.errorIfBlank
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asFailure
import com.procurement.clarification.lib.functional.asSuccess
import com.procurement.clarification.model.dto.ocds.Address
import com.procurement.clarification.model.dto.ocds.AddressDetails
import com.procurement.clarification.model.dto.ocds.CountryDetails
import com.procurement.clarification.model.dto.ocds.Enquiry
import com.procurement.clarification.model.dto.ocds.LocalityDetails
import com.procurement.clarification.model.dto.ocds.OrganizationReference
import com.procurement.clarification.model.dto.ocds.Period
import com.procurement.clarification.model.dto.ocds.RegionDetails
import com.procurement.clarification.model.dto.ocds.Tender
import com.procurement.clarification.utils.toJson
import com.procurement.clarification.utils.toLocal
import com.procurement.clarification.utils.toObject
import com.procurement.clarification.utils.tryToObject
import org.springframework.stereotype.Service

interface EnquiryService {
    fun addAnswer(cm: CommandMessage): ResponseDto
    fun checkAnswer(cm: CommandMessage): ResponseDto
    fun checkEnquiries(cm: CommandMessage): ResponseDto
    fun createEnquiry(cm: CommandMessage): ResponseDto
    fun findEnquiries(params: FindEnquiriesParams): Result<List<FindEnquiriesResult>, Fail>
    fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail>
}

@Service
class EnquiryServiceImpl(
    private val generationService: GenerationService,
    private val enquiryRepository: EnquiryRepository,
    private val periodService: PeriodService
) : EnquiryService {

    override fun addAnswer(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val token = cm.token
        val owner = cm.owner
        val enquiryId = cm.ctxId
        val dateTime = cm.startDate
        val dto = toObject(AddAnswerRq::class.java, cm.data)

        val entity = enquiryRepository.findBy(cpid, ocid, token)
            .onFailure { throw it.reason.exception }
            ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)

        val periodEntity = periodService.getPeriodEntity(cpid, ocid)
        if (periodEntity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
        if (entity.isAnswered) throw ErrorException(ErrorType.ALREADY_HAS_ANSWER)
        val enquiryDto = dto.enquiry
        val enquiry = toObject(Enquiry::class.java, entity.jsonData)
        if (enquiryId != enquiry.id) throw ErrorException(ErrorType.INVALID_ID)
        if (enquiryDto.answer.isBlank()) throw ErrorException(ErrorType.INVALID_ANSWER)

        val updatedEnquiry = enquiry.copy(
            answer = enquiryDto.answer,
            dateAnswered = dateTime
        )
        val updatedEntity = entity.copy(
            isAnswered = true,
            jsonData = toJson(updatedEnquiry)
        )
        enquiryRepository.save(updatedEntity)

        return ResponseDto(data = AddAnswerRs(updatedEnquiry))
    }

    override fun checkAnswer(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val token = cm.token
        val owner = cm.owner
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(ErrorType.CONTEXT)
        toObject(AddAnswerRq::class.java, cm.data)
        val entities = enquiryRepository.findBy(cpid, ocid)
            .onFailure { throw it.reason.exception }
        val isAllAnswered = !entities.any { (it.token != token && !it.isAnswered) }
        val periodEntity = periodService.getPeriodEntity(cpid, ocid)
        if (periodEntity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
        val endDate = periodEntity.endDate
        val isEnquiryPeriodExpired = (dateTime >= endDate)
        val setUnsuspended = isEnquiryPeriodExpired && isAllAnswered
        return ResponseDto(data = CheckAnswerRs(setUnsuspended))
    }

    override fun checkEnquiries(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val dateTime = cm.startDate
        val enquiryPeriod = periodService.getPeriodEntity(cpid, ocid)
        val startDate = enquiryPeriod.startDate
        val endDate = enquiryPeriod.endDate
        val isEnquiryPeriodExpired = (dateTime >= endDate)
        val isAllAnswered = checkIsAllAnswered(cpid, ocid)
        return ResponseDto(
            data = CheckEnquiresRs(
                isEnquiryPeriodExpired = isEnquiryPeriodExpired,
                tender = Tender(Period(startDate, endDate)),
                allAnswered = isAllAnswered
            )
        )
    }

    override fun createEnquiry(cm: CommandMessage): ResponseDto {
        val cpid = cm.cpid
        val ocid = cm.ocid
        val dateTime = cm.startDate
        val country = cm.country
        val pmd = cm.pmd

        val dto = toObject(CreateEnquiryRq::class.java, cm.data)
        dto.validateTextAttributes()

        periodService.checkDateInPeriod(dateTime, cpid, ocid, country, pmd)
        val periodEntity = periodService.getPeriodEntity(cpid, ocid)

        val enquiryRequest = dto.enquiry
        val owner = periodEntity.owner
        val author = converterOrganizationReferenceCreateToOrganizationReference(enquiryRequest.author)
        val enquiry = Enquiry(
            id = generationService.generateEnquiryId().toString(),
            date = dateTime,
            author = author,
            title = enquiryRequest.title,
            description = enquiryRequest.description,
            answer = null,
            relatedItem = enquiryRequest.relatedItem,
            relatedLot = enquiryRequest.relatedLot,
            dateAnswered = null
        )

        val entity = EnquiryEntity(
            cpid = cpid,
            ocid = ocid,
            token = generationService.generateToken(),
            isAnswered = false,
            jsonData = toJson(enquiry)
        )
        enquiryRepository.save(entity)
        return ResponseDto(data = CreateEnquiryRs(entity.token.toString(), owner, enquiry))
    }

    override fun findEnquiryIds(params: FindEnquiryIdsParams): Result<List<EnquiryId>, Fail> {
        val enquiryEntities = enquiryRepository.findBy(params.cpid, params.ocid)
            .onFailure { return it }

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
                    .onFailure {
                        return Fail.Incident.DatabaseIncident(exception = it.reason.exception)
                            .asFailure()
                    }
                    .let { it.id!! }
            }
            .asSuccess()
    }

    override fun findEnquiries(params: FindEnquiriesParams): Result<List<FindEnquiriesResult>, Fail> {

        val isAnswered = params.isAnswer

        val enquiryEntities = enquiryRepository.findBy( params.cpid, params.ocid)
            .onFailure { return it }

        val filteredEnquiries = if (isAnswered != null) {
            enquiryEntities.filter { it.isAnswered == isAnswered }
        } else {
            enquiryEntities
        }

        return filteredEnquiries
            .map { entity ->
                entity.jsonData
                    .tryToObject(Enquiry::class.java)
                    .onFailure {
                        return Fail.Incident.DatabaseIncident(exception = it.reason.exception)
                            .asFailure()
                    }
                    .convertToFindEnquiriesResult()
            }
            .asSuccess()
    }

    private fun CreateEnquiryRq.validateTextAttributes() {
        enquiry.apply {
            title.checkForBlank("enquiry.title")
            description.checkForBlank("enquiry.description")
            relatedItem.checkForBlank("enquiry.relatedItem")
            relatedLot.checkForBlank("enquiry.relatedLot")
            author.apply {
                name.checkForBlank("enquiry.author.name")

                identifier.apply {
                    scheme.checkForBlank("enquiry.author.identifier.scheme")
                    id.checkForBlank("enquiry.author.identifier.id")
                    legalName.checkForBlank("enquiry.author.identifier.legalName")
                    uri.checkForBlank("enquiry.author.identifier.uri")
                }

                address.apply {
                    streetAddress.checkForBlank("enquiry.author.address.streetAddress")
                    postalCode.checkForBlank("enquiry.author.address.postalCode")

                    addressDetails.apply {
                        locality.scheme.checkForBlank("enquiry.author.address.addressDetails.locality.scheme")
                        locality.id.checkForBlank("enquiry.author.address.addressDetails.locality.id")
                        locality.description.checkForBlank("enquiry.author.address.addressDetails.locality.description")
                        locality.uri.checkForBlank("enquiry.author.address.addressDetails.locality.uri")
                    }
                }

                additionalIdentifiers?.forEachIndexed { additionalIdentifierIdx, additionalIdentifier ->
                    additionalIdentifier.scheme.checkForBlank("enquiry.author.additionalIdentifiers[$additionalIdentifierIdx].scheme")
                    additionalIdentifier.id.checkForBlank("enquiry.author.additionalIdentifiers[$additionalIdentifierIdx].id")
                    additionalIdentifier.legalName.checkForBlank("enquiry.author.additionalIdentifiers[$additionalIdentifierIdx].legalName")
                    additionalIdentifier.uri.checkForBlank("enquiry.author.additionalIdentifiers[$additionalIdentifierIdx].uri")
                }

                contactPoint.apply {
                    name.checkForBlank("enquiry.author.contactPoint.name")
                    email.checkForBlank("enquiry.author.contactPoint.email")
                    telephone.checkForBlank("enquiry.author.contactPoint.telephone")
                    faxNumber.checkForBlank("enquiry.author.contactPoint.faxNumber")
                    url.checkForBlank("enquiry.author.contactPoint.url")
                }

                details.apply {
                    scale.checkForBlank("tenderer.details.scale")
                }
            }
        }
    }

    private fun String?.checkForBlank(name: String) = this.errorIfBlank {
        ErrorException(
            error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
            message = "The attribute '$name' is empty or blank."
        )
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

    private fun converterOrganizationReferenceCreateToOrganizationReference(author: OrganizationReferenceCreate): OrganizationReference {
        return OrganizationReference(
            name = author.name,
            id = author.identifier.scheme + "-" + author.identifier.id,
            identifier = author.identifier,
            address = author.address.let { address ->
                Address(
                    streetAddress = address.streetAddress,
                    postalCode = address.postalCode,
                    addressDetails = address.addressDetails.let { addressDetails ->
                        AddressDetails(
                            country = addressDetails.country.let { country ->
                                CountryDetails(
                                    id = country.id,
                                    scheme = country.scheme,
                                    description = country.description,
                                    uri = country.uri
                                )
                            },
                            region = addressDetails.region.let { region ->
                                RegionDetails(
                                    id = region.id,
                                    uri = region.uri,
                                    description = region.description,
                                    scheme = region.scheme
                                )
                            },
                            locality = addressDetails.locality.let { locality ->
                                LocalityDetails(
                                    id = locality.id,
                                    scheme = locality.scheme,
                                    description = locality.description,
                                    uri = locality.uri
                                )
                            }
                        )
                    }
                )
            },
            additionalIdentifiers = author.additionalIdentifiers,
            contactPoint = author.contactPoint,
            details = author.details
        )
    }

    private fun checkIsAllAnswered(cpid: Cpid, ocid: Ocid): Boolean {
        val isAllAnswered = enquiryRepository.findBy(cpid, ocid)
            .onFailure { throw it.reason.exception }
            .count { !it.isAnswered }
        return isAllAnswered == 0
    }
}
