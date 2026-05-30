package kite1412.portaltik

import androidx.compose.runtime.Composable

@Composable
expect fun rememberFilePicker(acceptedMimeTypes: Array<String>, maxFileSize: FileSize): FilePicker

interface FilePicker {
    suspend fun pickFile(): PickResult
}

sealed interface PickResult {
    data class Success(val file: File) : PickResult
    data class Failed(val reason: FailReason) : PickResult
}

sealed class FailReason(val message: String?) {
    object UnsupportedMimeType : FailReason(message = "Jenis file tidak didukung")
    data class FileSizeExceeded(val maxFileSize: FileSize) : FailReason(message = "Maksimal ukuran file $maxFileSize")
    object Unknown : FailReason(null)
}

data class File(
    val name: String,
    val mimeType: String?,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as File

        if (name != other.name) return false
        if (mimeType != other.mimeType) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}

@JvmInline
value class FileSize(
    val bytes: Long
) {
    override fun toString(): String =
        when {
            bytes >= 1024L * 1024L * 1024L ->
                "%.2f GB".format(bytes / (1024.0 * 1024.0 * 1024.0))

            bytes >= 1024L * 1024L ->
                "%.2f MB".format(bytes / (1024.0 * 1024.0))

            bytes >= 1024L ->
                "%.2f KB".format(bytes / 1024.0)

            else ->
                "$bytes B"
        }
}

infix fun Long.isLargerThan(fileSize: FileSize) = this > fileSize.bytes