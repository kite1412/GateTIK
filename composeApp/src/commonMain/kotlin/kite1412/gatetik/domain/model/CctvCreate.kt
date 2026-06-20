package kite1412.gatetik.domain.model

import kite1412.gatetik.model.CctvType

data class CctvCreate(
    val cameraName: String,
    val path: String,
    val streamUrl: String,
    val type: CctvType
)
