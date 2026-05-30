package kite1412.portaltik

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
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
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private var lastLocation: LocationData? = null
    @delegate:SuppressLint("MissingPermission")
    private val requestLocationUpdates: SharedFlow<LocationState> by lazy {
        callbackFlow {
            if (!LocationPermissionController.isPermissionGranted()) {
                close()
                return@callbackFlow
            }
            trySend(LocationState.Loading)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(res: LocationResult) {
                    res.lastLocation?.toLocationData()?.let {
                        Logger.d(
                            tag = logTag,
                            message = "Current location: $it"
                        )
                        lastLocation = it
                        trySend(LocationState.Available(it))
                    }
                }
            }
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?
                ) {
                    if (!isLocationEnabled()) {
                        Logger.d(
                            tag = logTag,
                            message = "Location disabled"
                        )
                        trySend(LocationState.Unavailable)
                    } else lastLocation?.let { lastLocation ->
                        trySend(LocationState.Available(lastLocation))
                    }
                }
            }
            val filter = IntentFilter(LocationManager.MODE_CHANGED_ACTION)

            ContextCompat.registerReceiver(
                /*context =*/context,
                /*receiver =*/receiver,
                /*filter =*/filter,
                /*flags =*/ContextCompat.RECEIVER_NOT_EXPORTED
            )

            fusedLocationClient.requestLocationUpdates(
                /*p0 =*/LocationRequest.Builder(
                    /*priority =*/Priority.PRIORITY_HIGH_ACCURACY,
                    /*intervalMillis =*/5000L
                )
                    .setMinUpdateIntervalMillis(2000L)
                    .setMinUpdateDistanceMeters(5f)
                    .build(),
                /*p1 =*/locationCallback,
                /*p2*/Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
                context.unregisterReceiver(receiver)
            }
        }
            .shareIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(5000),
                replay = 1
            )
    }

    override fun observeLocationState(): Flow<LocationState> = requestLocationUpdates

    private fun isLocationEnabled(): Boolean {
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private fun Location.toLocationData() = LocationData(
        latitude = latitude,
        longitude = longitude
    )
}