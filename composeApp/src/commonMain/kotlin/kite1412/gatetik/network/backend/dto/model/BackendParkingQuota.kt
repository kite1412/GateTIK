package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.ParkingQuota
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendParkingQuota(
    @SerialName("total_slots")
    val totalSlots: Int,
    @SerialName("used_slots")
    val usedSlots: Int,
    @SerialName("auto_restrict_student")
    val autoRestrictStudent: Boolean,
    @SerialName("updated_at")
    val updatedAt: Instant? = null
) {
    fun toModel() = ParkingQuota(
        id = 1,
        totalSlots = totalSlots,
        usedSlots = usedSlots,
        autoRestrictStudents = autoRestrictStudent
    )
}
