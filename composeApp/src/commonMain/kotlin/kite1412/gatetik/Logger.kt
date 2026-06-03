package kite1412.gatetik

expect object Logger {
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun w(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
}