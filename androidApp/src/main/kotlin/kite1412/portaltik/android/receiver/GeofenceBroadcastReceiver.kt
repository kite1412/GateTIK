package kite1412.portaltik.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kite1412.portaltik.Logger

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val logTag = "GeofenceBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
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
                ) geofencingEvent.triggeringGeofences?.let { triggeringGeofences ->
                    // TODO send/dismiss gate trigger notification
                } else Logger.e(
                    tag = logTag,
                    message = "Invalid geofence transition type $geofenceTransition"
                )
            }
        }
    }
}