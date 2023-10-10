package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SettingsDao) {

    val settings: Flow<Settings?> = settingsDao.getAll()

    @WorkerThread
    suspend fun insertOrUpdateSettings(settings: Settings) {
        settingsDao.insertOrUpdate(settings)
    }
}
