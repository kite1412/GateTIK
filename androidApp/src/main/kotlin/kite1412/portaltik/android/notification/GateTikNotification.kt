package kite1412.portaltik.android.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kite1412.portaltik.Location
import kite1412.portaltik.android.R
import kite1412.portaltik.android.receiver.OpenGateReceiver

private const val MAIN_ACTIVITY_NAME = "kite1412.portaltik.android.MainActivity"

sealed class GateTikNotification(
    val channelId: String,
    val channelName: String,
    val channelDescription: String,
    val importance: Int
) {
    object GateAccess : GateTikNotification(
        channelId = "gate_access",
        channelName = "Gate Access",
        channelDescription = "Notifikasi buka gate",
        importance = NotificationManager.IMPORTANCE_HIGH
    ) {
        fun createNotification(
            context: Context,
            location: Location
        ): Notification = createNotification(
            context = context,
            contentIntent = createContentIntent(
                context = context,
                requestCode = 1
            )
        ) {
            addAction(
                /*icon =*/0,
                /*title =*/"Buka Gate",
                /*intent =*/OpenGateReceiver.pendingIntent(
                    context = context,
                    location = location
                )
            )
        }
    }

    fun createContentIntent(
        context: Context,
        requestCode: Int,
        data: Uri? = null
    ): PendingIntent = PendingIntent.getActivity(
        /*context = */context,
        /*requestCode = */requestCode,
        /*intent = */Intent().apply {
            action = Intent.ACTION_VIEW
            this.data = data
            component = ComponentName(
                context.packageName,
                MAIN_ACTIVITY_NAME
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        },
        /*flags = */PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    internal fun createNotification(
        context: Context,
        contentIntent: PendingIntent,
        block: NotificationCompat.Builder.() -> Unit
    ): Notification {
        ensureChannelExists(context)

        return NotificationCompat.Builder(context, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.door_open)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .apply(block)
            .build()
    }

    private fun ensureChannelExists(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                /*id = */channelId,
                /*name = */channelName,
                /*importance = */importance
            ).apply {
                description = channelDescription
            }

            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }
}