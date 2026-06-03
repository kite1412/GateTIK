package kite1412.gatetik

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual object NotificationPermissionController : KoinComponent {
    private val context by inject<Context>()

    actual fun isPermissionGranted(): Boolean =
        if (isAboveTiramisu())
            ActivityCompat.checkSelfPermission(
                /*context = */context,
                /*permission = */Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        else true

    actual fun getPermissionString(): String = if (isAboveTiramisu()) Manifest.permission.POST_NOTIFICATIONS
        else ""

    private fun isAboveTiramisu() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}