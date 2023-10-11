package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.repository.GoNapRepository

class SettingsViewModel(
    private val goNapRepository: GoNapRepository
) : ViewModel() {

    val settingsFlow = goNapRepository.settings.asLiveData()

    fun saveSettings(settings: Settings) = viewModelScope.launch {
        goNapRepository.insertOrUpdateSettings(settings)
    }
}
