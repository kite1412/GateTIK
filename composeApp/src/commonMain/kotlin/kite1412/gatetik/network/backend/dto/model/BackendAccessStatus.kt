package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.AccessStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackendAccessStatus {
    @SerialName("success")
    SUCCESS,
    @SerialName("failed")
    FAILED,
    @SerialName("pending")
    PENDING;

    fun toModel() = when (this) {
        SUCCESS -> AccessStatus.SUCCESS
        FAILED -> AccessStatus.FAILED
        PENDING -> AccessStatus.PENDING
    }
}
