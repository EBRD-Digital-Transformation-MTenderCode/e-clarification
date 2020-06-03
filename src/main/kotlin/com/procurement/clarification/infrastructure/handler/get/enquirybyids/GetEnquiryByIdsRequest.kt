package com.procurement.clarification.infrastructure.handler.get.enquirybyids

import com.fasterxml.jackson.annotation.JsonProperty

data class GetEnquiryByIdsRequest(
    @param:JsonProperty("cpid") @field:JsonProperty("cpid") val cpid: String,
    @param:JsonProperty("ocid") @field:JsonProperty("ocid") val ocid: String,
    @param:JsonProperty("enquiryIds") @field:JsonProperty("enquiryIds") val enquiryIds: List<String>
)
