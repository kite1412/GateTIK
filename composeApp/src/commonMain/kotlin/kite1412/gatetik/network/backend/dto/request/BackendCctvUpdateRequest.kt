package kite1412.gatetik.network.backend.dto.request

import kite1412.gatetik.network.backend.dto.model.BackendCctvType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendCctvUpdateRequest(
    @SerialName("camera_name")
    val cameraName: String? = null,
    val path: String? = null,
    @SerialName("stream_url")
    val streamUrl: String? = null,
    @SerialName("type")
    val type: BackendCctvType? = null
)
