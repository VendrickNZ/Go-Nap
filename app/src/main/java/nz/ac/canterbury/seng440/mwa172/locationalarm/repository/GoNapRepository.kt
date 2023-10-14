package nz.ac.canterbury.seng440.mwa172.locationalarm.repository

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

    fun getLatestAlarm() = alarmRepository.getLatestAlarm()


}