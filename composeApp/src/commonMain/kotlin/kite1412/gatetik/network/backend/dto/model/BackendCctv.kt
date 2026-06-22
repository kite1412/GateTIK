package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.Cctv
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
    val path: String,
    val type: BackendCctvType,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
) {
    fun toModel() = Cctv(
        id = id,
        cameraName = cameraName,
        streamUrl = streamUrl,
        path = path,
        isActive = true,
        type = type.toModel(),
        createdAt = createdAt
    )
}
