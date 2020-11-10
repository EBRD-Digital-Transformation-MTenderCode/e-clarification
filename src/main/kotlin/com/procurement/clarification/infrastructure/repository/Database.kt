package com.procurement.clarification.infrastructure.repository

object Database {
    const val KEYSPACE = "submission"

    object History {
        const val TABLE = "history"
        const val COMMAND_ID = "command_id"
        const val COMMAND_NAME = "command_name"
        const val COMMAND_DATE = "command_date"
        const val JSON_DATA = "json_data"
    }

    object Rules {
        const val TABLE = "rules"
        const val COUNTRY = "country"
        const val PMD = "pmd"
        const val OPERATION_TYPE = "operation_type"
        const val PARAMETER = "parameter"
        const val VALUE = "value"
    }

    object Period {
        const val TABLE = "periods"
        const val CPID = "cpid"
        const val OCID = "ocid"
        const val OWNER = "owner"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val TENDER_END_DATE = "tender_end_date"
    }

    object Enquiry {
        const val TABLE = "enquiries"
        const val CPID = "cpid"
        const val OCID = "ocid"
        const val TOKEN_ENTITY = "token_entity"
        const val IS_ANSWERED = "is_answered"
        const val JSON_DATA = "json_data"
    }
}
