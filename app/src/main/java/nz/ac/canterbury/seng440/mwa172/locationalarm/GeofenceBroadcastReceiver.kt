package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class GeofenceBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("GeoFenceReceiver", "Received geofence broadcast")
    }
}