package kite1412.gatetik.model

import kotlin.time.Instant

data class Cctv(
    val id: Int,
    val cameraName: String,
    val streamUrl: String,
    val path: String = "",
    val isActive: Boolean,
    val createdAt: Instant = Instant.DISTANT_PAST,
    val type: CctvType
)
