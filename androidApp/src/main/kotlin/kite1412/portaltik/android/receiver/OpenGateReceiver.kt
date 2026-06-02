package kite1412.portaltik.android.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kite1412.portaltik.Location
import kite1412.portaltik.Logger
import kite1412.portaltik.common.AppCoroutineScope
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.SessionStatus
import kite1412.portaltik.domain.repository.GateRepository
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class OpenGateReceiver : BroadcastReceiver(), KoinComponent {
    private val logTag = "OpenGateReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val pendingResult = goAsync()
        val appScope = get<AppCoroutineScope>()
        val gateRepository = get<GateRepository>()
        val authentication = get<Authentication>()

        appScope.launch {
            val sessionStatus = authentication.sessionStatus.first()

            if (sessionStatus is SessionStatus.SignedIn) {
                val user = sessionStatus.user

                gateRepository.getMainGate()
                    .onError {
                        Logger.w(
                            tag = logTag,
                            message = "Failed to open gate from notification: ${it.message}"
                        )
                        pendingResult.finish()
                    }
                    .onSuccess {
                        it?.let { gate ->
                            if (user.role == UserRole.STUDENT) {
                                val latitude = intent.getDoubleExtra(LATITUDE, 200.0)
                                val longitude = intent.getDoubleExtra(LONGITUDE, 200.0)

                                if (listOf(latitude, longitude).max() == 200.0) {
                                    pendingResult.finish()
                                    return@launch
                                }

                                gateRepository.enterGate(
                                    id = gate.id,
                                    location = Location(
                                        latitude = latitude,
                                        longitude = longitude
                                    )
                                )
                            } else gateRepository.openGate(gate.id)
                            Logger.d(
                                tag = logTag,
                                message = "Success opening gate from notification"
                            )
                        }
                        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                            .cancel(1)
                        pendingResult.finish()
                    }
            } else pendingResult.finish()
        }
    }

    companion object {
        private const val ACTION = "kite1412.portaltik.android.OPEN_GATE_ACTION"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"

        fun pendingIntent(
            context: Context,
            location: Location
        ): PendingIntent = PendingIntent.getBroadcast(
            /*context =*/context,
            /*requestCode =*/1,
            /*intent =*/Intent(context, OpenGateReceiver::class.java).apply {
                this.action = ACTION
                putExtra(LATITUDE, location.latitude)
                putExtra(LONGITUDE, location.longitude)
            },
            /*flags =*/PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}