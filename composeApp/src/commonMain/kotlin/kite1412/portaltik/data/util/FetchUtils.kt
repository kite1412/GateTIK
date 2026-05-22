package kite1412.portaltik.data.util

import kite1412.portaltik.Logger
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result
import kite1412.portaltik.util.Success
import kite1412.portaltik.util.Unknown

suspend fun <T> tryOrThrowUnknown(
    logTag: String,
    errorMessage: String,
    action: suspend () -> T
): Result<T, Error> = try {
    Success(action())
} catch (e: Exception) {
    Logger.e(logTag, errorMessage, e)
    Error(Unknown(errorMessage))
}