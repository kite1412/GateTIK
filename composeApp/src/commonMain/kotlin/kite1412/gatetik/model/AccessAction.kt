package kite1412.gatetik.model

enum class AccessAction {
    OPEN, CLOSE, ENTRY, EXIT;

    val capitalizedName = this.name.lowercase().replaceFirstChar { it.uppercase() }
}