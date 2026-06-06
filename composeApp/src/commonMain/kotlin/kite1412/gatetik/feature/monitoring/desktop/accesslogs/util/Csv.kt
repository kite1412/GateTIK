package kite1412.gatetik.feature.monitoring.desktop.accesslogs.util

import kite1412.gatetik.CsvWriter
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.util.timestampString

suspend fun List<AccessLog>.writeToCsv(writer: CsvWriter) {
    with(writer) {
        writeLine("Pengguna,Role,Aksi,Metode,Status,Catatan,Timestamp")

        this@writeToCsv.forEach { log ->
            with(log) {
                writeLine(
                    listOf(
                        userFullName,
                        userRole.toIdString(),
                        action.capitalizedName,
                        accessMethod.capitalizedName,
                        status.capitalizedName,
                        notes ?: "",
                        createdAt.timestampString
                    ).joinToString(separator = ",") { escapeCsv(it) }
                )
            }
        }
    }
}

private fun escapeCsv(value: Any?): String {
    val text = value?.toString() ?: ""

    return if (
        text.contains(",") ||
        text.contains("\"") ||
        text.contains("\n")
    ) {
        "\"${text.replace("\"", "\"\"")}\""
    } else {
        text
    }
}