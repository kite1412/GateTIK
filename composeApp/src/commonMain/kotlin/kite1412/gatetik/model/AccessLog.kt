package kite1412.gatetik.model

import kotlin.time.Instant

data class AccessLog(
    val id: Int,
    val userId: Int,
    val gateId: Int,
    val status: AccessStatus,
    val accessMethod: AccessMethod,
    val action: AccessAction,
    val notes: String?,
    val updatedAt: Instant,
    val createdAt: Instant
)