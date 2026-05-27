package kite1412.portaltik.network.backend.dto.model

import kite1412.portaltik.model.Cctv
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendCctv(
    val id: Int,
    @SerialName("camera_name")
    val cameraName: String,
    @SerialName("stream_url")
    val streamUrl: String,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
) {
    fun toModel() = Cctv(
        id = id,
        cameraName = cameraName,
        streamUrl = streamUrl,
        isActive = isActive
    )
}
