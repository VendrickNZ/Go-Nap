package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.repository.AppRepository

class SettingsViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    val settingsFlow = appRepository.settings.asLiveData()

    fun saveSettings(settings: Settings) = viewModelScope.launch {
        appRepository.insertOrUpdateSettings(settings)
    }
}
