package kite1412.portaltik

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kite1412.portaltik.common.AppCoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kite1412.portaltik.Location as LocationData

class AndroidLocationService(
    appScope: AppCoroutineScope,
    private val context: Context
) : LocationService {
    private val logTag = "PortalTikLocationService"
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    @delegate:SuppressLint("MissingPermission")
    private val requestLocationUpdates: SharedFlow<LocationState> by lazy {
        callbackFlow {
            if (!LocationPermissionController.isPermissionGranted()) {
                close()
                return@callbackFlow
            }

            val callback = object : LocationCallback() {
                override fun onLocationResult(res: LocationResult) {
                    res.lastLocation?.toLocationData()?.let {
                        Logger.d(
                            tag = logTag,
                            message = "Current location: $it"
                        )
                        trySend(LocationState.Available(it))
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                /*p0 =*/LocationRequest.Builder(
                    /*priority =*/Priority.PRIORITY_HIGH_ACCURACY,
                    /*intervalMillis =*/5000L
                )
                    .setMinUpdateIntervalMillis(2000L)
                    .setMinUpdateDistanceMeters(5f)
                    .build(),
                /*p1 =*/callback,
                /*p2*/Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }
            .shareIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(5000),
                replay = 1
            )
    }

    override fun observeLocationState(): Flow<LocationState> = requestLocationUpdates

    private fun Location.toLocationData() = LocationData(
        latitude = latitude,
        longitude = longitude
    )
}