package kite1412.portaltik.android.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kite1412.portaltik.Location
import kite1412.portaltik.Logger
import kite1412.portaltik.android.notification.GateTikNotification

class GeofenceReceiver : BroadcastReceiver() {
    private val logTag = "GeofenceReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.d(
            tag = logTag,
            message = "Geofencing event received"
        )
        if (intent != null && context != null) if (
            ActivityCompat.checkSelfPermission(
                /*context =*/context,
                /*permission =*/Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent != null) {
                if (geofencingEvent.hasError()) {
                    val errorMessage = GeofenceStatusCodes
                        .getStatusCodeString(geofencingEvent.errorCode)
                    Logger.e(
                        tag = logTag,
                        message = errorMessage
                    )
                    return
                }

                val geofenceTransition = geofencingEvent.geofenceTransition

                if (
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
                ) {
                    Logger.d(
                        tag = logTag,
                        message = "Geofence event received: ${if (geofenceTransition == 1) "ENTER" else "EXIT"}"
                    )
                    val notificationId = 1
                    val notificationManager = NotificationManagerCompat.from(context)

                    if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
                        notificationManager.cancel(notificationId)
                    else geofencingEvent.triggeringGeofences?.let { triggeringGeofences ->
                        triggeringGeofences.firstOrNull()?.let { geofence ->
                            notificationManager.notify(
                                /*id =*/notificationId,
                                /*notification =*/GateTikNotification.GateAccess.createNotification(
                                    context = context,
                                    location = Location(
                                        latitude = geofence.latitude,
                                        longitude = geofence.longitude
                                    )
                                )
                            )
                        }
                    }
                } else Logger.e(
                    tag = logTag,
                    message = "Invalid geofence transition type $geofenceTransition"
                )
            }
        }
    }
}