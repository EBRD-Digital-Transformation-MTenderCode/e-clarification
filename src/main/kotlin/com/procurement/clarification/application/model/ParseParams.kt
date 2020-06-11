package com.procurement.clarification.application.model

import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asSuccess

fun parseCpid(value: String): Result<Cpid, DataErrors.Validation.DataMismatchToPattern> =
    Cpid.tryCreate(value = value)
        .doReturn { expectedPattern ->
            return Result.failure(
                DataErrors.Validation.DataMismatchToPattern(
                    name = "cpid",
                    pattern = expectedPattern,
                    actualValue = value
                )
            )
        }
        .asSuccess()

fun parseOcid(value: String): Result<Ocid, DataErrors.Validation.DataMismatchToPattern> =
    Ocid.tryCreate(value = value)
        .doReturn { expectedPattern ->
            return Result.failure(
                DataErrors.Validation.DataMismatchToPattern(
                    name = "ocid",
                    pattern = expectedPattern,
                    actualValue = value
                )
            )
        }
        .asSuccess()
