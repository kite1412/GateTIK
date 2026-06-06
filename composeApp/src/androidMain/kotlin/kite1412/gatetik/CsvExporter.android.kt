package kite1412.gatetik

class UnsupportedAndroidCsvExporter : CsvExporter {
    override suspend fun export(
        filename: String,
        write: suspend CsvWriter.() -> Unit
    ): CsvExportResult = CsvExportResult.Failed("CSV export is not supported")
}