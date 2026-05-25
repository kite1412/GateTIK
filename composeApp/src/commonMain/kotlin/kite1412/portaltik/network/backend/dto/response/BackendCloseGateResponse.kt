package kite1412.portaltik.network.backend.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendCloseGateResponse(
    val action: String,
    @SerialName("closed_at")
    val closedAt: Instant,
    @SerialName("access_log_id")
    val accessLogId: Int
)
