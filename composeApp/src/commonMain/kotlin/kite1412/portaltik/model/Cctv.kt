package kite1412.portaltik.model

data class Cctv(
    val id: Int,
    val cameraName: String,
    val streamUrl: String,
    val isActive: Boolean
)
