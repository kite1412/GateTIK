package kite1412.portaltik.model

enum class GateStatus {
    OPEN,
    CLOSED,
    OPENING,
    OFFLINE;

    fun toIdString(): String = when (this) {
        OPEN -> "Terbuka"
        CLOSED -> "Tertutup"
        OPENING -> "Sedang Membuka"
        OFFLINE -> "Offline"
    }
}