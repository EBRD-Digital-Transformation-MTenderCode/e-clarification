package com.procurement.clarification.infrastructure.api.v1

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(
    val id: String? = null,
    val errors: List<ResponseErrorDto>? = null,
    val data: Any? = null
)
