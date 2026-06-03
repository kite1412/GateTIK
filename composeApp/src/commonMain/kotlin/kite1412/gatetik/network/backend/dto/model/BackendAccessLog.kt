package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.AccessLog
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendAccessLog(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("gate_id")
    val gateId: Int,
    @SerialName("access_status")
    val accessStatus: BackendAccessStatus,
    @SerialName("access_method")
    val accessMethod: BackendAccessMethod,
    val action: BackendAccessAction,
    val notes: String?,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val user: BackendUserMinimum? = null,
    val gate: BackendGateMinimum? = null
) {
    fun toModel() = AccessLog(
        id = id,
        userId = userId,
        gateId = gateId,
        status = accessStatus.toModel(),
        accessMethod = accessMethod.toModel(),
        action = action.toModel(),
        notes = notes,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}

@Serializable
data class BackendUserMinimum(
    val id: Int,
    @SerialName("full_name")
    val fullName: String,
    val role: BackendUserRole
)

@Serializable
data class BackendGateMinimum(
    val id: Int,
    @SerialName("gate_name")
    val gateName: String
)