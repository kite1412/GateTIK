package kite1412.gatetik.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun now(): Instant = Clock.System.now()

fun Instant.toLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime = toLocalDateTime(timeZone)

fun Instant.timeAgo(now: Instant = now()): String {
    val seconds = now.epochSeconds - this.epochSeconds

    return when {
        seconds < 60 -> "Baru saja"
        seconds < 3600 -> "${seconds / 60} menit lalu"
        seconds < 86400 -> "${seconds / 3600} jam lalu"
        seconds < 604800 -> "${seconds / 86400} hari lalu"
        seconds < 2592000 -> "${seconds / 604800} minggu lalu"
        seconds < 31536000 -> "${seconds / 2592000} bulan lalu"
        else -> "${seconds / 31536000} tahun lalu"
    }
}