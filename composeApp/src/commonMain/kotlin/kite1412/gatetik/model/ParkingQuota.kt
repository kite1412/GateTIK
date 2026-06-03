package kite1412.gatetik.model

data class ParkingQuota(
    val id: Int,
    val totalSlots: Int,
    val usedSlots: Int,
    val autoRestrictStudents: Boolean
)
