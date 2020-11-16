package com.procurement.clarification.application.model

import com.procurement.clarification.domain.EnumElementProvider
import com.procurement.clarification.domain.EnumElementProvider.Companion.keysAsStrings
import com.procurement.clarification.domain.extension.tryParseLocalDateTime
import com.procurement.clarification.domain.fail.error.DataErrors
import com.procurement.clarification.domain.fail.error.DataTimeError
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.Owner
import com.procurement.clarification.domain.model.enums.OperationType
import com.procurement.clarification.domain.model.enums.ProcurementMethod
import com.procurement.clarification.domain.model.tryOwner
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.lib.functional.asSuccess
import java.time.LocalDateTime

fun parseCpid(value: String): Result<Cpid, DataErrors.Validation.DataMismatchToPattern> =
    Cpid.tryCreateOrNull(value = value)
        ?.asSuccess()
        ?: Result.failure(
            DataErrors.Validation.DataMismatchToPattern(
                name = "cpid",
                pattern = Cpid.pattern,
                actualValue = value
            )
        )

fun parseOcid(value: String): Result<Ocid, DataErrors.Validation.DataMismatchToPattern> =
    Ocid.tryCreateOrNull(value = value)
        ?.asSuccess()
        ?: Result.failure(
            DataErrors.Validation.DataMismatchToPattern(
                name = "ocid",
                pattern = Ocid.pattern,
                actualValue = value
            )
        )

fun parseOwner(value: String): Result<Owner, DataErrors.Validation.DataFormatMismatch> =
    value.tryOwner()
        .mapFailure {
            DataErrors.Validation.DataFormatMismatch(
                name = "owner",
                expectedFormat = "uuid",
                actualValue = value
            )
        }

fun parseDate(value: String, attributeName: String): Result<LocalDateTime, DataErrors.Validation> =
    value.tryParseLocalDateTime()
        .mapFailure { fail ->
            when (fail) {
                is DataTimeError.InvalidFormat -> DataErrors.Validation.DataFormatMismatch(
                    name = attributeName,
                    actualValue = value,
                    expectedFormat = fail.pattern
                )

                is DataTimeError.InvalidDateTime ->
                    DataErrors.Validation.InvalidDateTime(name = attributeName, actualValue = value)
            }
        }

fun parseOperationType(
    value: String, allowedEnums: Set<OperationType>, attributeName: String = "operationType"
): Result<OperationType, DataErrors> =
    parseEnum(value = value, allowedEnums = allowedEnums, attributeName = attributeName, target = OperationType)

fun parsePmd(
    value: String, allowedEnums: Set<ProcurementMethod>, attributeName: String = "pmd"
): Result<ProcurementMethod, DataErrors> =
    parseEnum(value = value, allowedEnums = allowedEnums, attributeName = attributeName, target = ProcurementMethod)

private fun <T> parseEnum(
    value: String, allowedEnums: Set<T>, attributeName: String, target: EnumElementProvider<T>
): Result<T, DataErrors.Validation.UnknownValue> where T : Enum<T>,
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