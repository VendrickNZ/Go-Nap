package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        val Tag: String = AlarmReceiver::class.simpleName!!
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationChannelId = "geofence_alarm_channel"

        // Create an explicit intent for an Activity in your app
        val alarmIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)


        // Create a notification channel if on Android Oreo or higher
        val name = "Geofence Alarm"
        val description = "Channel for Geofence Alarm"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(notificationChannelId, name, importance)
        channel.description = description

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.mipmap.ic_launcher)  // replace with your own icon
            .setContentTitle(context.resources.getString(R.string.notification_title))
            .setContentText(context.resources.getString(R.string.notification_description))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(false)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

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