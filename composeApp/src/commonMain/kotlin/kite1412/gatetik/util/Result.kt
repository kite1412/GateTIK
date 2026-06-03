package kite1412.gatetik.util

sealed interface Result<out T, out E: Error> {
    object Loading : Result<Nothing, Nothing>
    data class Success<out T>(val data: T) : Result<T, Nothing>
    data class Error<out E: kite1412.gatetik.util.Error>(
        val error: E
    ) : Result<Nothing, E>
}

inline fun <T, E: Error> Result<T, E>.onSuccess(
    block: (T) -> Unit
): Result<T, E> {
    if (this is Result.Success) block(data)
    return this
}

inline fun <T, E: Error> Result<T, E>.onError(
    block: (E) -> Unit
): Result<T, E> {
    if (this is Result.Error) block(error)
    return this
}

inline fun <T, E: Error> Result<T, E>.onLoading(
    block: () -> Unit
): Result<T, E> {
    if (this is Result.Loading) block()
    return this
}

fun <T> Success(data: T) = Result.Success(data)

fun <E: Error> Error(error: E) = Result.Error(error)