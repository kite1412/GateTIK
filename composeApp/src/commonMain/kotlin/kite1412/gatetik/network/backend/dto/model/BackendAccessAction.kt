package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.AccessAction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackendAccessAction {
    @SerialName("open")
    OPEN,
    @SerialName("close")
    CLOSE,
    @SerialName("entry")
    ENTRY,
    @SerialName("exit")
    EXIT;

    fun toModel() = when (this) {
        OPEN -> AccessAction.OPEN
        CLOSE -> AccessAction.CLOSE
        ENTRY -> AccessAction.ENTRY
        EXIT -> AccessAction.EXIT
    }
}