package kite1412.gatetik.network.mock

import kite1412.gatetik.model.AccessAction
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessMethod
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.Gate
import kite1412.gatetik.model.IotDevice
import kite1412.gatetik.model.IotDeviceStatus
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.util.now
import kotlin.time.Instant

val mockUser = User(
    id = 0,
    fullName = "Mock User",
    email = "mock@gatetik.com",
    role = UserRole.ADMIN,
    status = UserStatus.ACTIVE,
    institutionNumber = "mock-1",
    createdAt = now()
)

val mockIotDevice = IotDevice(
    id = 1,
    deviceName = "ESP32 Main Gate Controller",
    deviceUid = "ESP32-MAIN-001",
    gateId = 1,
    firmwareVersion = "v1.2.4",
    status = IotDeviceStatus.ONLINE,
    lastOnlineAt = Instant.parse("2026-05-22T10:15:30Z")
)

val mockGate = Gate(
    id = 1,
    gateName = "Main Entrance Gate",
    latitude = -5.35723145,
    longitude = 105.31578291,
    allowedRadiusMeter = 50,
    iotDevice = mockIotDevice
)

val mockCctv = Cctv(
    id = 1,
    cameraName = "Lobby Camera",
    streamUrl = "rtsp://192.168.1.10:554/stream1",
    isActive = true
)

val mockParkingQuota = ParkingQuota(
    id = 1,
    totalSlots = 100,
    usedSlots = 87,
    autoRestrictStudents = true
)

val mockAccessLogs = listOf(
    AccessLog(
        id = 1,
        userId = 101,
        userFullName = "john.doe",
        userRole = UserRole.STUDENT,
        gateId = 1,
        status = AccessStatus.SUCCESS,
        accessMethod = AccessMethod.MOBILE,
        action = AccessAction.OPEN,
        notes = "Access granted normally",
        updatedAt = Instant.parse("2026-05-23T07:15:30Z"),
        createdAt = Instant.parse("2026-05-23T07:15:35Z")
    ),
    AccessLog(
        id = 2,
        userId = 102,
        userFullName = "sarah.smith",
        userRole = UserRole.STUDENT,
        gateId = 2,
        status = AccessStatus.PENDING,
        accessMethod = AccessMethod.MOBILE,
        action = AccessAction.OPEN,
        notes = "Face mismatch detected",
        updatedAt = Instant.parse("2026-05-23T08:02:10Z"),
        createdAt = Instant.parse("2026-05-23T08:02:15Z")
    ),
    AccessLog(
        id = 3,
        userId = 103,
        userFullName = "michael.brown",
        userRole = UserRole.STAFF,
        gateId = 1,
        status = AccessStatus.SUCCESS,
        accessMethod = AccessMethod.MOBILE,
        action = AccessAction.CLOSE,
        notes = null,
        updatedAt = Instant.parse("2026-05-23T09:40:00Z"),
        createdAt = Instant.parse("2026-05-23T09:40:03Z")
    ),
    AccessLog(
        id = 4,
        userId = 104,
        userFullName = "admin.jane",
        userRole = UserRole.ADMIN,
        gateId = 1,
        status = AccessStatus.FAILED,
        accessMethod = AccessMethod.MOBILE,
        action = AccessAction.CLOSE,
        notes = "Waiting for admin approval",
        updatedAt = Instant.parse("2026-05-23T10:12:45Z"),
        createdAt = Instant.parse("2026-05-23T10:12:50Z")
    ),
    AccessLog(
        id = 5,
        userId = 105,
        userFullName = "david.wilson",
        userRole = UserRole.STAFF,
        gateId = 1,
        status = AccessStatus.SUCCESS,
        accessMethod = AccessMethod.MOBILE,
        action = AccessAction.OPEN,
        notes = "Fingerprint verified",
        updatedAt = Instant.parse("2026-05-23T11:05:20Z"),
        createdAt = Instant.parse("2026-05-23T11:05:25Z")
    )
)