package kite1412.portaltik

interface LocationPermissionController {
    fun isPermissionGranted(): Boolean
    fun getPermissionString(): String
}
