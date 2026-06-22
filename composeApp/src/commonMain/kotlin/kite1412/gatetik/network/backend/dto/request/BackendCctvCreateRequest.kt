package kite1412.gatetik.network.backend.dto.request

import kite1412.gatetik.network.backend.dto.model.BackendCctvType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendCctvCreateRequest(
    @SerialName("camera_name")
    val cameraName: String,
    val path: String,
    @SerialName("stream_url")
    val streamUrl: String,
    val type: BackendCctvType
)
