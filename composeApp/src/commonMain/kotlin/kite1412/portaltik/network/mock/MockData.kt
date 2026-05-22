package kite1412.portaltik.network.mock

import kite1412.portaltik.model.Cctv
import kite1412.portaltik.model.Gate
import kite1412.portaltik.model.GateStatus
import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.model.IotDeviceStatus
import kite1412.portaltik.model.ParkingQuota
import kotlin.time.Instant

val mockGate = Gate(
    id = 1,
    gateName = "Main Entrance Gate",
    latitude = -5.35723145,
    longitude = 105.31578291,
    allowedRadiusMeter = 50,
    currentStatus = GateStatus.OPEN,
    isActive = true
)

val mockIotDevice = IotDevice(
    id = 1,
    deviceName = "ESP32 Main Gate Controller",
    deviceUid = "ESP32-MAIN-001",
    gateId = mockGate.id.toString(),
    ipAddress = "192.168.1.100",
    firmwareVersion = "v1.2.4",
    status = IotDeviceStatus.ONLINE,
    lastOnlineAt = Instant.parse("2026-05-22T10:15:30Z")
)

val mockCctv = Cctv(
    id = 1,
    cameraName = "Lobby Camera",
    streamUrl = "rtsp://192.168.1.10:554/stream1",
    isActive = true
)

val mockParkingQuota = ParkingQuota(
    id = "PARKING-QUOTA-001",
    totalSlots = 120,
    usedSlots = 87,
    autoRestrictStudents = true
)