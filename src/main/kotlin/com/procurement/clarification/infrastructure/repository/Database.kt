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
}
