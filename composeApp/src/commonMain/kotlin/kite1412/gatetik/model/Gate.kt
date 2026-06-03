package kite1412.gatetik.model

data class Gate(
    val id: Int,
    val gateName: String,
    val latitude: Double,
    val longitude: Double,
    val allowedRadiusMeter: Int,
    val iotDevice: IotDevice
)
