package kite1412.portaltik

class UnsupportedJvmLocationPermissionController : LocationPermissionController {
    override fun isPermissionGranted(): Boolean = false

    override fun getPermissionString(): String = ""
}