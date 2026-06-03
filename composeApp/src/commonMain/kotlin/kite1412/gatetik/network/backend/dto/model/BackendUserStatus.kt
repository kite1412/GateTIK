package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.UserStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackendUserStatus {
    @SerialName("pending")
    PENDING,
    @SerialName("active")
    ACTIVE,
    @SerialName("suspended")
    SUSPENDED;

    fun toModel(): UserStatus = UserStatus.valueOf(this.name)
}