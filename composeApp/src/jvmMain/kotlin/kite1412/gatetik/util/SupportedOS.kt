package kite1412.gatetik.util

fun getSupportedOS(): SupportedOS {
    val os = System.getProperty("os.name").lowercase()
    return when {
        os.contains("win") -> SupportedOS.WINDOWS
        os.contains("mac") -> SupportedOS.MACOS
        os.contains("nix") ||
            os.contains("nux") ||
            os.contains("aix") -> SupportedOS.LINUX
        else -> throw OSNotSupportedException()
    }
}

enum class SupportedOS(val osName: String) {
    WINDOWS("Windows"),
    MACOS("macOS"),
    LINUX("Linux")
}

class OSNotSupportedException : RuntimeException() {
    override val message: String
        get() = "Operating System is not supported"
}