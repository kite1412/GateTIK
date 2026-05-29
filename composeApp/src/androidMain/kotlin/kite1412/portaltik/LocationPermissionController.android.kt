package kite1412.portaltik

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class AndroidLocationPermissionController(
    private val context: Context
) : LocationPermissionController {
    override fun isPermissionGranted(): Boolean =
        ActivityCompat.checkSelfPermission(
            /*context = */context,
            /*permission = */getPermissionString()
        ) == PackageManager.PERMISSION_GRANTED

    override fun getPermissionString(): String =
        Manifest.permission.ACCESS_FINE_LOCATION
}