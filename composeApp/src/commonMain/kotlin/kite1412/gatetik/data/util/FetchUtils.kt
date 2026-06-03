package kite1412.gatetik.data.util

import kite1412.gatetik.Logger
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.Success
import kite1412.gatetik.util.Unknown

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


suspend fun <T> tryOrThrowUnknown(
    logTag: String,
    errorMessage: String,
    action: suspend (throwError: () -> Nothing) -> T
): Result<T, Error> = try {
    Success(
        action {
            throw RuntimeException()
        }
    )
} catch (e: Exception) {
    Logger.e(logTag, errorMessage, e)
    Error(Unknown(errorMessage))
}