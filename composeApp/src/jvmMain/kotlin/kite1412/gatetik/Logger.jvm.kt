package kite1412.gatetik

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

actual object Logger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        logger.error(throwable) { logMessage(tag, message) }
    }

    actual fun w(tag: String, message: String) {
        logger.warn { logMessage(tag, message) }
    }

    actual fun d(tag: String, message: String) {
        logger.debug { logMessage(tag, message) }
    }

    actual fun i(tag: String, message: String) {
        logger.info { logMessage(tag, message) }
    }

    private fun logMessage(tag: String, message: String) = "[$tag]: $message"
}