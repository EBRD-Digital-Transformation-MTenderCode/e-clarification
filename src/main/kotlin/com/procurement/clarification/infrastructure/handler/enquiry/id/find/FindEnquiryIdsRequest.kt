package com.procurement.clarification.infrastructure.handler.enquiry.id.find

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class FindEnquiryIdsRequest(
    @param:JsonProperty("cpid") @field:JsonProperty("cpid") val cpid: String,
    @param:JsonProperty("ocid") @field:JsonProperty("ocid") val ocid: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("isAnswer") @get:JsonProperty("isAnswer") val isAnswer: Boolean?
)
