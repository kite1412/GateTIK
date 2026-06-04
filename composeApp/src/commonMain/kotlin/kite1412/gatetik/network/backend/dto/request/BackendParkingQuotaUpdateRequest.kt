package kite1412.gatetik.network.backend.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendParkingQuotaUpdateRequest(
    @SerialName("total_slots")
    val totalSlots: Int? = null,
    @SerialName("auto_restrict_student")
    val autoRestrictStudent: Boolean? = null
)
