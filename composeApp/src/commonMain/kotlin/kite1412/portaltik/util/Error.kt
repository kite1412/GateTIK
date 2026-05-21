package kite1412.portaltik.util

/**
 * An error with user-friendly message.
 */
interface Error {
    val message: String
}

data class Unknown(
    override val message: String = "Unknown Error"
) : Error