package kite1412.portaltik.model

import kotlin.time.Instant

data class AccessLog(
    val id: Int,
    val userId: Int,
    val gateId: Int,
    val status: AccessStatus,
    val accessMethod: String?,
    val triggeredBy: String,
    val notes: String?,
    val accessedAt: Instant,
    val createdAt: Instant
)