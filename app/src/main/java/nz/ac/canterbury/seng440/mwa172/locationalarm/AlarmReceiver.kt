package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        val Tag: String = AlarmReceiver::class.simpleName!!
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(Tag, "Create alarm?")
    }
}