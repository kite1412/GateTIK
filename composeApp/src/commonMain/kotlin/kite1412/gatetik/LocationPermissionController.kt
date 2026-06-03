package kite1412.gatetik

expect object LocationPermissionController {
    fun isPermissionGranted(): Boolean
    fun getPermissionString(): String
}
