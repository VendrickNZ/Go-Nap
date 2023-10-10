package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.app.Application
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmDatabase
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmRepository
import nz.ac.canterbury.seng440.mwa172.locationalarm.repository.AppRepository
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsRepository

class GoNapApplication : Application() {

    val database by lazy {
        AlarmDatabase.getDatabase(this)
    }
    val appRepository by lazy {
        AppRepository(
            AlarmRepository(database.alarmDao()),
            SettingsRepository(database.settingsDao())
        )
    }
}