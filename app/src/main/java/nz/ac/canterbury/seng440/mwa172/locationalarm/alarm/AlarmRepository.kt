package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {

    val alarms: Flow<List<Alarm>> = alarmDao.getAll()

    @WorkerThread
    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

}