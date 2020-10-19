package com.procurement.clarification.infrastructure.handler.enquiry.period


import com.fasterxml.jackson.annotation.JsonProperty

data class CreateEnquiryPeriodRequest(
    @param:JsonProperty("cpid") @field:JsonProperty("cpid") val cpid: String,
    @param:JsonProperty("ocid") @field:JsonProperty("ocid") val ocid: String,
    @param:JsonProperty("tender") @field:JsonProperty("tender") val tender: Tender,
    @param:JsonProperty("owner") @field:JsonProperty("owner") val owner: String,
    @param:JsonProperty("pmd") @field:JsonProperty("pmd") val pmd: String,
    @param:JsonProperty("country") @field:JsonProperty("country") val country: String,
    @param:JsonProperty("operationType") @field:JsonProperty("operationType") val operationType: String
) {
    data class Tender(
        @param:JsonProperty("tenderPeriod") @field:JsonProperty("tenderPeriod") val tenderPeriod: TenderPeriod
    ) {
        data class TenderPeriod(
            @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: String,
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: String
        )
    }
}
