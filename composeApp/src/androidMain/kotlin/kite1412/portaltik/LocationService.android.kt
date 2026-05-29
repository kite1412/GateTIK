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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kite1412.portaltik.Location as LocationData

class AndroidLocationService(
    private val context: Context,
    private val permissionController: AndroidLocationPermissionController
) : LocationService {
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Flow<LocationData> = callbackFlow {
        if (!permissionController.isPermissionGranted()) {
            close()
            return@callbackFlow
        }

        val callback = object : LocationCallback() {
            override fun onLocationResult(res: LocationResult) {
                res.lastLocation?.toLocationData()?.let(::trySend)
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

    private fun Location.toLocationData() = LocationData(
        latitude = latitude,
        longitude = longitude
    )
}