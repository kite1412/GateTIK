package kite1412.portaltik.network.backend.dto.model

import kite1412.portaltik.model.AccessMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackendAccessMethod {
    @SerialName("mobile")
    MOBILE,
    @SerialName("web")
    WEB,
    @SerialName("desktop")
    DESKTOP;

    fun toModel() = when (this) {
        MOBILE -> AccessMethod.MOBILE
        WEB -> AccessMethod.WEB
        DESKTOP -> AccessMethod.DESKTOP
    }
}