package kite1412.gatetik

import androidx.compose.ui.awt.ComposeWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.io.BufferedWriter
import java.io.File

class JvmCsvExporter(
    private val window: ComposeWindow
) : CsvExporter {
    private val logTag = "CsvExporter"

    override suspend fun export(
        filename: String,
        write: suspend CsvWriter.() -> Unit
    ): CsvExportResult {
        return try {
            val file = showSaveDialog(window, filename)
                ?: return CsvExportResult.Cancelled("Ekspor dibatalkan")

            withContext(Dispatchers.IO) {
                file.bufferedWriter().use {
                    write(JvmCsvWriter(it))
                }
            }

            CsvExportResult.Success
        } catch (e: Exception) {
            Logger.e(
                tag = logTag,
                message = "Gagal mengekspor CSV",
                throwable = e
            )
            CsvExportResult.Failed("Gagal mengekspor CSV")
        }
    }

    private fun showSaveDialog(
        window: ComposeWindow,
        defaultName: String
    ): File? {
        val dialog = FileDialog(
            window,
            "Ekspor CSV",
            FileDialog.SAVE
        ).apply {
            file = defaultName
            isVisible = true
        }

        val selectedFile = dialog.file ?: return null

        return File(
            dialog.directory,
            if (selectedFile.endsWith(".csv"))
                selectedFile
            else
                "$selectedFile.csv"
        )
    }
}

class JvmCsvWriter(
    private val writer: BufferedWriter
) : CsvWriter {
    override suspend fun writeLine(line: String) {
        writer.appendLine(line)
    }
}