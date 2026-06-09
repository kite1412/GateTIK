package kite1412.gatetik.data.util

import kite1412.gatetik.Logger
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.onError

fun <T> Result<T, Error>.logError(logTag: String) =
    onError {
        Logger.e(
            tag = logTag,
            message = it.message
        )
    }