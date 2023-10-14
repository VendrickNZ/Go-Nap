package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val NotificationChannelId: String = "go_nap_alarms"

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        val Tag: String = AlarmReceiver::class.simpleName!!
    }

    override fun onReceive(context: Context, intent: Intent) {

        // Create an explicit intent for an Activity in your app
        val alarmIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channel = NotificationChannel(
            NotificationChannelId,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = context.getString(R.string.notification_channel_desc)
        channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

        channel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        )

        channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.createNotificationChannel(channel)

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context, NotificationChannelId)
            .setSmallIcon(R.mipmap.ic_launcher)  // replace with your own icon
            .setContentTitle(context.resources.getString(R.string.notification_title))
            .setContentText(context.resources.getString(R.string.notification_description))
            .setAutoCancel(false)
            .setFullScreenIntent(pendingIntent, true)
            .setContentIntent(pendingIntent)

        // Show the notification
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(Tag, "No permission for notifications :(")
            return
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}