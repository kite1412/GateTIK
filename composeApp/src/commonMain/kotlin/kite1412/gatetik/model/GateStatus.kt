package kite1412.gatetik.model

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