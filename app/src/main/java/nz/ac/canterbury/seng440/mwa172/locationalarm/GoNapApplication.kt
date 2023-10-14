package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.app.Application
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmRepository
import nz.ac.canterbury.seng440.mwa172.locationalarm.repository.GoNapDatabase
import nz.ac.canterbury.seng440.mwa172.locationalarm.repository.GoNapRepository

class GoNapApplication : Application() {

    val goNapDatabase by lazy {
        GoNapDatabase.getDatabase(this)
    }
    val goNapRepository by lazy {
        GoNapRepository(
            AlarmRepository(goNapDatabase.alarmDao()),
        )
    }
    val state by lazy {
        GoNapState()
    }
}