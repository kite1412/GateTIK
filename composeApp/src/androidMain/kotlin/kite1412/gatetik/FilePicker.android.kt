package kite1412.gatetik

import android.annotation.SuppressLint
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("Recycle")
@Composable
actual fun rememberFilePicker(
    acceptedMimeTypes: Array<String>,
    maxFileSize: FileSize
): FilePicker {
    val context = LocalContext.current
    var callback by remember {
        mutableStateOf<((PickResult) -> Unit)?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) {
            callback?.invoke(PickResult.Failed(FailReason.Unknown))
            callback = null
            return@rememberLauncherForActivityResult
        }

        val resolver = context.contentResolver
        val document = runCatching {
            val query = resolver
                .query(uri, null, null, null, null)

            val name = query
                ?.use { cursor ->
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                    if (cursor.moveToFirst()) {
                        val size = cursor.getLong(sizeIndex)
                        if (size isLargerThan maxFileSize) {
                            callback?.invoke(PickResult.Failed(FailReason.FileSizeExceeded(maxFileSize)))
                            callback = null
                            return@rememberLauncherForActivityResult
                        }
                    }

                    if (cursor.moveToFirst()) cursor.getString(nameIndex)
                    else null
                }
            val mimeType = resolver.getType(uri)

            if (mimeType !in acceptedMimeTypes) {
                callback?.invoke(PickResult.Failed(FailReason.UnsupportedMimeType))
                callback = null
                return@rememberLauncherForActivityResult
            }

            File(
                name = name ?: "unknown",
                mimeType = mimeType,
                bytes = resolver.openInputStream(uri)!!.use {
                    it.readBytes()
                }
            )
        }
            .getOrNull()


        callback?.invoke(
            if (document != null) PickResult.Success(document)
            else PickResult.Failed(FailReason.Unknown)
        )
        callback = null
    }

    return remember(acceptedMimeTypes, maxFileSize) {
        AndroidFilePicker { onRes ->
            callback = onRes
            launcher.launch(acceptedMimeTypes)
        }
    }
}

class AndroidFilePicker(
    private val launcherPicker: (
        onResult: (PickResult) -> Unit
    ) -> Unit
) : FilePicker {
    override suspend fun pickFile(): PickResult =
        suspendCancellableCoroutine { cont ->
            launcherPicker { res ->
                cont.resume(res)
            }
        }
}