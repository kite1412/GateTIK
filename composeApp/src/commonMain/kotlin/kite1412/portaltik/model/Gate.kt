package kite1412.portaltik.model

data class Gate(
    val id: Int,
    val gateName: String,
    val latitude: Double,
    val longitude: Double,
    val allowedRadiusMeter: Int,
    val currentStatus: GateStatus,
    val isActive: Boolean
)
