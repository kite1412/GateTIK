package kite1412.gatetik

import androidx.compose.runtime.compositionLocalOf

// Each platform must provide this local composition instance
val LocalCsvExporter = compositionLocalOf<CsvExporter> {
    throw NotImplementedError("CsvExporter is not implemented by platform")
}

interface CsvExporter {
    suspend fun export(
        filename: String,
        write: suspend CsvWriter.() -> Unit
    ): CsvExportResult
}

interface CsvWriter {
    suspend fun writeLine(line: String)
}

sealed interface CsvExportResult {
    object Success : CsvExportResult
    data class Failed(val reason: String) : CsvExportResult
    data class Cancelled(val reason: String) : CsvExportResult
}