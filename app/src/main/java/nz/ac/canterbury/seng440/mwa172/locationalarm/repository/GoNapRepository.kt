package nz.ac.canterbury.seng440.mwa172.locationalarm.repository

import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmRepository

class GoNapRepository(
    private val alarmRepository: AlarmRepository
) {

    val alarms = alarmRepository.alarms

    suspend fun insertAlarm(alarm: Alarm) {
        alarmRepository.insert(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmRepository.delete(alarm)
    }

    fun getAlarmById(id: Long): Flow<Alarm?> = alarmRepository.getAlarmById(id)

    fun getLatestAlarm() = alarmRepository.getLatestAlarm()


}