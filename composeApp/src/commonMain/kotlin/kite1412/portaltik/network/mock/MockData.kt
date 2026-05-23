package kite1412.portaltik.network.mock

import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.model.AccessStatus
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
    currentStatus = GateStatus.CLOSED,
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

val mockAccessLogs = listOf(
    AccessLog(
        id = 1,
        userId = 0,
        gateId = 1,
        status = AccessStatus.SUCCESS,
        accessMethod = "RFID",
        triggeredBy = "System",
        notes = "Access granted normally",
        accessedAt = Instant.parse("2026-05-23T07:15:30Z"),
        createdAt = Instant.parse("2026-05-23T07:15:35Z")
    ),
    AccessLog(
        id = 2,
        userId = 0,
        gateId = 2,
        status = AccessStatus.DENIED,
        accessMethod = "Face Recognition",
        triggeredBy = "Security",
        notes = "Face mismatch detected",
        accessedAt = Instant.parse("2026-05-23T08:02:10Z"),
        createdAt = Instant.parse("2026-05-23T08:02:15Z")
    ),
    AccessLog(
        id = 3,
        userId = 103,
        gateId = 1,
        status = AccessStatus.SUCCESS,
        accessMethod = "QR Code",
        triggeredBy = "Mobile App",
        notes = null,
        accessedAt = Instant.parse("2026-05-23T09:40:00Z"),
        createdAt = Instant.parse("2026-05-23T09:40:03Z")
    ),
    AccessLog(
        id = 4,
        userId = 104,
        gateId = 1,
        status = AccessStatus.FAILED,
        accessMethod = null,
        triggeredBy = "Manual Override",
        notes = "Waiting for admin approval",
        accessedAt = Instant.parse("2026-05-23T10:12:45Z"),
        createdAt = Instant.parse("2026-05-23T10:12:50Z")
    ),
    AccessLog(
        id = 5,
        userId = 105,
        gateId = 1,
        status = AccessStatus.SUCCESS,
        accessMethod = "Fingerprint",
        triggeredBy = "System",
        notes = "Fingerprint verified",
        accessedAt = Instant.parse("2026-05-23T11:05:20Z"),
        createdAt = Instant.parse("2026-05-23T11:05:25Z")
    )
)