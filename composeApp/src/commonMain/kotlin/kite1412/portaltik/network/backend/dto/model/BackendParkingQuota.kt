package kite1412.portaltik.network.backend.dto.model

import kite1412.portaltik.model.ParkingQuota
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendParkingQuota(
    @SerialName("total_slots")
    val totalSlots: Int,
    @SerialName("used_slots")
    val usedSlots: Int,
    @SerialName("available_slots")
    val availableSlots: Int,
    @SerialName("auto_restrict_student")
    val autoRestrictStudent: Boolean
) {
    fun toModel() = ParkingQuota(
        id = 1,
        totalSlots = totalSlots,
        usedSlots = usedSlots,
        autoRestrictStudents = autoRestrictStudent
    )
}
