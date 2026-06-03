package kite1412.gatetik.model

data class Cctv(
    val id: Int,
    val cameraName: String,
    val streamUrl: String,
    val isActive: Boolean
)
