package kite1412.portaltik.model

enum class GateStatus {
    OPEN,
    CLOSED,
    OPENING,
    OFFLINE;

    fun toIdString(): String = when (this) {
        GateStatus.OPEN -> "Terbuka"
        GateStatus.CLOSED -> "Tertutup"
        GateStatus.OPENING -> "Sedang Membuka"
        GateStatus.OFFLINE -> "Offline"
    }
}