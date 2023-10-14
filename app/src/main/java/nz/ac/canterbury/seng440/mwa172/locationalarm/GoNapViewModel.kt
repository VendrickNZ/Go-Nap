package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.repository.GoNapRepository
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings
import org.jetbrains.annotations.Contract

class GoNapViewModelFactory(
    private val goNapRepository: GoNapRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoNapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoNapViewModel(goNapRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class GoNapViewModel(
    private val goNapRepository: GoNapRepository
) : ViewModel() {

    private var _location: MutableLiveData<Location> = MutableLiveData()

    val alarms: LiveData<List<Alarm>> = goNapRepository.alarms.asLiveData()

    val currentName = mutableStateOf("My Alarm")
    val currentRadius = mutableDoubleStateOf(50.0)

    val tempName = MutableLiveData<String>()
    val tempRadius = MutableLiveData<Double>()

    val location: LiveData<Location>
        get() = _location

    fun addAlarm(alarm: Alarm) = viewModelScope.launch {
        goNapRepository.insertAlarm(alarm)
        Log.d(Tag, "Inserted new alarm: $alarm")
    }

    fun removeAlarm(alarm: Alarm) = viewModelScope.launch {
        goNapRepository.deleteAlarm(alarm)
    }

    fun getAlarmById(id: Long): Flow<Alarm?> {
        return goNapRepository.getAlarmById(id)
    }

    fun getLatestAlarm(): LiveData<Alarm?> {
        return goNapRepository.getLatestAlarm().asLiveData()
    }


    companion object {
        const val MinDistanceForUpdate: Float = 10f
        private const val Tag: String = "MapViewModel"
    }


    @Synchronized
    fun updateLocation(location: Location) {
        _location.value?.let {
            if (it.distanceTo(location) <= MinDistanceForUpdate) {
                Log.d(Tag, "User has not moved sufficiently to update location")
                return
            }
        }

        Log.d(Tag, "Updating location in view model")
        _location.value = location
    }

    @RequiresPermission(anyOf = [
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION"
    ])
    suspend fun startLocationUpdates(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        while (true) {
            Log.d(Tag,"Waiting for next update")
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { it: Location? ->
                    if (it != null) {
                        Log.d(Tag, "Latitude: ${it.latitude}, Longitude: ${it.longitude}")
                        updateLocation(it)
                    }
                }
            delay(3000)
        }
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