package nz.ac.canterbury.seng440.mwa172.locationalarm.repository

import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmRepository
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsRepository

class GoNapRepository(
    private val alarmRepository: AlarmRepository,
    private val settingsRepository: SettingsRepository
) {

    val alarms = alarmRepository.alarms
    val settings = settingsRepository.settings

    suspend fun insertAlarm(alarm: Alarm) {
        alarmRepository.insert(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmRepository.delete(alarm)
    }

    suspend fun insertOrUpdateSettings(settings: Settings) {
        settingsRepository.insertOrUpdateSettings(settings)
    }

    fun getLatestAlarm() = alarmRepository.getLatestAlarm()

    suspend fun initializeDefaultSettingsIfNecessary() {
        settingsRepository.initializeDefaultSettingsIfNecessary()
    }

}