package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SettingsDao) {

    val settings: Flow<Settings?> = settingsDao.getSettings()

    @WorkerThread
    suspend fun insertOrUpdateSettings(settings: Settings) {
        settingsDao.insertOrUpdate(settings)
    }

    @WorkerThread
    suspend fun initializeDefaultSettingsIfNecessary() {
        val existingSettings = settingsDao.getSettingsOnce()
        if (existingSettings == null) {
            val defaultSettings = Settings(
                defaultName = "My Alarm",
                defaultRadius = 100.0,
                defaultVibration = true,
                defaultSound = "someUrl"
            )
            settingsDao.insertOrUpdate(defaultSettings)
        }
    }
}
