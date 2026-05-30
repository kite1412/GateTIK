package kite1412.portaltik

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberFilePicker(
    acceptedMimeTypes: Array<String>,
    maxFileSize: FileSize
): FilePicker = remember(acceptedMimeTypes, maxFileSize) {
    JvmFilePicker(
        acceptedMimeTypes = acceptedMimeTypes,
        maxFileSize = maxFileSize
    )
}

class JvmFilePicker(
    private val acceptedMimeTypes: Array<String>,
    private val maxFileSize: FileSize
) : FilePicker {
    override suspend fun pickFile(): PickResult {
        val chooser = JFileChooser().apply {
            fileFilter = FileNameExtensionFilter(
                "Images",
                *acceptedMimeTypes
                    .filter { it.contains('/') }
                    .map { it.substringAfter('/') }
                    .toTypedArray()
            )

            isAcceptAllFileFilterUsed = false
        }
        val result = chooser.showOpenDialog(null)

        if (result != JFileChooser.APPROVE_OPTION) {
            return PickResult.Failed(FailReason.UnsupportedMimeType)
        }

        val file = chooser.selectedFile
        if (file.length() > maxFileSize.bytes) {
            return PickResult.Failed(FailReason.FileSizeExceeded(maxFileSize))
        }
        val mimeType = withContext(Dispatchers.IO) {
            Files.probeContentType(
                file.toPath()
            )
        }
        if (mimeType !in acceptedMimeTypes)
            return PickResult.Failed(FailReason.UnsupportedMimeType)

        return PickResult.Success(
            File(
                name = file.name,
                mimeType = mimeType,
                bytes = file.readBytes()
            )
        )
    }
}