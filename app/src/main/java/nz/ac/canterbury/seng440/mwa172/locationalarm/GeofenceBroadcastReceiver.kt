package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private val tag: String = GeofenceBroadcastReceiver::class.simpleName!!
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(tag, "Received geofence broadcast")
        val app = (context.applicationContext as GoNapApplication)
        app.state.activeAlarm = null

        setAlarm(context)
    }

    private fun setAlarm(context: Context) {
        Log.d(tag, "Setting alarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val basicPendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

        val clockInfo = AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 100, basicPendingIntent)

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAlarmClock(
                clockInfo,
                pendingIntent
            )
        }

    }
}