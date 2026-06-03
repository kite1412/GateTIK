package kite1412.gatetik.model

enum class IotDeviceStatus {
    ONLINE, OFFLINE;

    val capitalizedName = this.name.lowercase().replaceFirstChar { it.uppercase() }
}