package kite1412.portaltik.model

import kotlin.time.Instant

data class IotDevice(
    val id: Int,
    val deviceName: String,
    val deviceUid: String,
    val gateId: String,
    val ipAddress: String,
    val firmwareVersion: String,
    val status: IotDeviceStatus,
    val lastOnlineAt: Instant
)