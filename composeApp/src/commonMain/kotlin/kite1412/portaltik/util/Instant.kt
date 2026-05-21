package kite1412.portaltik.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun now(): Instant = Clock.System.now()

fun Instant.toLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime = toLocalDateTime(timeZone)