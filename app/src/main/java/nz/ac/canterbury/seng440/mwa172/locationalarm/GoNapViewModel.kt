package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.repository.AppRepository
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsViewModel
import org.jetbrains.annotations.Contract

class GoNapViewModelFactory(
    private val appRepository: AppRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoNapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoNapViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(appRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class GoNapViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    private var _location: MutableLiveData<Location> = MutableLiveData()

    val settingsFlow = appRepository.settings.asLiveData()
    val alarms: LiveData<List<Alarm>> = appRepository.alarms.asLiveData()

    val location: LiveData<Location>
        get() = _location


    fun addAlarm(alarm: Alarm) = viewModelScope.launch {
        appRepository.insertAlarm(alarm)
        Log.d(Tag, "Inserted new alarm: $alarm")
    }

    fun removeAlarm(alarm: Alarm) = viewModelScope.launch {
        appRepository.deleteAlarm(alarm)
    }

    fun getLatestAlarm(): LiveData<Alarm?> {
        return appRepository.getLatestAlarm().asLiveData()
    }

     fun updateRadius(newRadius: Double) = viewModelScope.launch {
        val currentSettings = settingsFlow.value ?: Settings(
            defaultRadius = newRadius,
            defaultName = "Default name",
            defaultSound = "Default sound",
            defaultVibration = true
        )
        val updatedSettings = currentSettings.copy(defaultRadius = newRadius)
        appRepository.insertOrUpdateSettings(updatedSettings)
    }

    companion object {
        const val MinDistanceForUpdate: Float = 10f
        private const val Tag: String = "MapViewModel"
    }

    fun updateLocation(location: Location) {
        _location.value?.let {
            if (it.distanceTo(location) <= MinDistanceForUpdate) {
                Log.d(Tag, "User has not moved sufficiently to update crosshair")
                return
            }
        }

        Log.d(Tag, "Updating location in view model")
        _location.value = location

    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(fusedLocationProviderClient: FusedLocationProviderClient) {

        val locationRequest: LocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3000
        ).build()

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(Tag, "Received a location update")
                val lastLocation = locationResult.locations.last()
                Log.d(Tag, "Latitude: ${lastLocation.latitude}, Longitude: ${lastLocation.longitude}")
                updateLocation(
                    locationResult.locations
                        .last()
                )
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}

@Contract("->new")
fun Location?.asLatLng(): LatLng {
    return if (this === null) {
        LatLng(0.0, 0.0)
    } else {
        LatLng(this.latitude, this.longitude)
    }
}