package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class GeofenceBroadcastReceiver: BroadcastReceiver() {

    companion object {
        private val tag: String = GeofenceBroadcastReceiver::class.simpleName!!
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(tag, "Received geofence broadcast")
        val app = (context.applicationContext as GoNapApplication)

        app.state.activeAlarm = null
    }
}