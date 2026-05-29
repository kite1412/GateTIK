package kite1412.portaltik

expect object LocationPermissionController {
    fun isPermissionGranted(): Boolean
    fun getPermissionString(): String
}
