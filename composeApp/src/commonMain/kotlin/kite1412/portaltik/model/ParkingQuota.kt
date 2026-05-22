package kite1412.portaltik.model

data class ParkingQuota(
    val id: String,
    val totalSlots: Int,
    val usedSlots: Int,
    val autoRestrictStudents: Boolean
)
