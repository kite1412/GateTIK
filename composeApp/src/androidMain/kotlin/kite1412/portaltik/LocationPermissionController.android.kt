package kite1412.portaltik

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual object LocationPermissionController : KoinComponent {
    private val context by inject<Context>()

    actual fun isPermissionGranted(): Boolean =
        ActivityCompat.checkSelfPermission(
            /*context = */context,
            /*permission = */getPermissionString()
        ) == PackageManager.PERMISSION_GRANTED

    actual fun getPermissionString(): String =
        Manifest.permission.ACCESS_FINE_LOCATION
}