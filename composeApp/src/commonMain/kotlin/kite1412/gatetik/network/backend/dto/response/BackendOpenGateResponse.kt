package kite1412.gatetik.network.backend.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendOpenGateResponse(
    val action: String,
    @SerialName("opened_at")
    val openedAt: Instant,
    @SerialName("access_log_id")
    val accessLogId: Int
)
