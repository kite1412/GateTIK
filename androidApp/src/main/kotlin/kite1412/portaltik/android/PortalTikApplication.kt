package kite1412.portaltik.android

import android.Manifest
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kite1412.portaltik.Logger
import kite1412.portaltik.android.receiver.GeofenceBroadcastReceiver
import kite1412.portaltik.common.AppCoroutineScope
import kite1412.portaltik.di.initKoin
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.SessionStatus
import kite1412.portaltik.domain.repository.GateRepository
import kite1412.portaltik.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext

class PortalTikApplication : Application() {
    private val logTag = "PortalTikApplication"
    private val appScope by lazy { get<AppCoroutineScope>() }
    private val authentication by lazy { get<Authentication>() }
    private val gateRepository by lazy { get<GateRepository>() }

    private val geofencePendingIntent: PendingIntent by lazy {
        val flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags or PendingIntent.FLAG_MUTABLE
        }
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, flags)
    }

    lateinit var geofencingClient: GeofencingClient

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        geofencingClient = LocationServices.getGeofencingClient(this)

        initKoin {
            androidContext(this@PortalTikApplication)
        }

        if (
            ActivityCompat.checkSelfPermission(
                /*context = */this,
                /*permission = */Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            authentication
                .sessionStatus
                .mapLatest { status ->
                    if (status is SessionStatus.SignedIn) {
                        val res = gateRepository.getMainGate()

                        if (res is Result.Success) res.data?.let { gate ->
                            geofencingClient.addGeofences(
                                /*p0 = */createGeofencingRequest(
                                    latitude = gate.latitude,
                                    longitude = gate.longitude,
                                    radiusInMeters = gate.allowedRadiusMeter
                                ),
                                /*p1 = */geofencePendingIntent
                            ).run {
                                addOnSuccessListener {
                                    Logger.i(
                                        tag = logTag,
                                        message = "Success adding geofencing request"
                                    )
                                }
                                addOnFailureListener {
                                    Logger.i(
                                        tag = logTag,
                                        message = "Failed to add geofencing request"
                                    )
                                }
                            }
                        }
                    }
                }
                .launchIn(appScope)
        }
    }

    private fun createGeofencingRequest(latitude: Double, longitude: Double, radiusInMeters: Int): GeofencingRequest =
        GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(
                listOf(
                    createGeofence(
                        latitude = latitude,
                        longitude = longitude,
                        radiusInMeters = radiusInMeters
                    )
                )
            )
        }.build()

    private fun createGeofence(latitude: Double, longitude: Double, radiusInMeters: Int): Geofence =
        Geofence.Builder()
            .setRequestId("main-gate")
            .setCircularRegion(
                /*latitude = */latitude,
                /*longitude = */longitude,
                /*radius = */radiusInMeters.toFloat()
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
}