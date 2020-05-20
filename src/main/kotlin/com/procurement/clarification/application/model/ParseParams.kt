package com.procurement.clarification.application.model

import com.procurement.clarification.domain.EnumElementProvider
import com.procurement.clarification.domain.EnumElementProvider.Companion.keysAsStrings
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.date.tryParseLocalDateTime
import com.procurement.clarification.domain.model.token.Token
import com.procurement.clarification.domain.model.token.tryCreateToken
import com.procurement.clarification.domain.util.Result
import com.procurement.clarification.domain.util.asFailure
import com.procurement.clarification.domain.util.asSuccess
import java.time.LocalDateTime

fun parseCpid(value: String): Result<Cpid, DataErrors.Validation.DataMismatchToPattern> =
    Cpid.tryCreate(value = value)
        .doOnError { expectedPattern ->
            return Result.failure(
                DataErrors.Validation.DataMismatchToPattern(
                    name = "cpid",
                    pattern = expectedPattern,
                    actualValue = value
                )
            )
        }
        .get
        .asSuccess()

fun parseOcid(value: String): Result<Ocid, DataErrors.Validation.DataMismatchToPattern> =
    Ocid.tryCreate(value = value)
        .doOnError { expectedPattern ->
            return Result.failure(
                DataErrors.Validation.DataMismatchToPattern(
                    name = "ocid",
                    pattern = expectedPattern,
                    actualValue = value
                )
            )
        }
        .get
        .asSuccess()

fun parseToken(value: String): Result<Token, DataErrors.Validation.DataFormatMismatch> =
    value.tryCreateToken()
        .doOnError { pattern ->
            return DataErrors.Validation.DataFormatMismatch(
                actualValue = value,
                name = "token",
                expectedFormat = pattern
            ).asFailure()
        }
        .get
        .asSuccess()

fun parseStartDate(value: String): Result<LocalDateTime, DataErrors.Validation.DataFormatMismatch> =
    value.tryParseLocalDateTime()
        .doOnError { expectedFormat ->
            return Result.failure(
                DataErrors.Validation.DataFormatMismatch(
                    name = "startDate",
                    actualValue = value,
                    expectedFormat = expectedFormat
                )
            )
        }
        .get
        .asSuccess()

fun <T> parseEnum(value: String, allowedEnums: Set<T>, attributeName: String, target: EnumElementProvider<T>)
    : Result<T, DataErrors.Validation.UnknownValue> where T : Enum<T>,
                                                          T : EnumElementProvider.Key =
    target.orNull(value)
        ?.takeIf { it in allowedEnums }
        ?.asSuccess()
        ?: Result.failure(
            DataErrors.Validation.UnknownValue(
                name = attributeName,
                expectedValues = allowedEnums.keysAsStrings(),
                actualValue = value
            )
        )
