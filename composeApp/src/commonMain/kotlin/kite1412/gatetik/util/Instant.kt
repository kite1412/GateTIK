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

val Instant.timestampString: String
    get() {
        val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = localDateTime.day
        val monthStr = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        val year = localDateTime.year

        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')

        return "$day $monthStr $year, $hour:$minute:$second"
    }

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