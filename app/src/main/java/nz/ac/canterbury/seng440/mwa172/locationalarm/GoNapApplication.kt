package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.app.Application
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmDatabase
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmRepository

class GoNapApplication: Application() {

    val database by lazy {
        AlarmDatabase.getDatabase(this)
    }
    val repository by lazy {
        AlarmRepository(database.alarmDao())
    }
}