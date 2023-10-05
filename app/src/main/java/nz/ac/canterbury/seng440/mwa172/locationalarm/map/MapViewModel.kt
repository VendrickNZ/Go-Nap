package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.annotations.Contract

class MapViewModel : ViewModel() {

    private var _location: MutableLiveData<Location> = MutableLiveData()


    val location: LiveData<Location>
        get() = _location
    companion object {
        const val MIN_DISTANCE_FOR_UPDATE: Float = 10f
    }

    fun updateLocation(location: Location) {
        if (_location.value !== null && _location.value!!.distanceTo(location) <= MIN_DISTANCE_FOR_UPDATE) {
            return;
        }
        _location.value = location
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(fusedLocationProviderClient: FusedLocationProviderClient) {
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations){
                    updateLocation(location)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun liveLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): Flow<LatLng> {
        return flow {
            while (true) {
                val location: Location = suspendCancellableCoroutine { continuation ->
                    fusedLocationProviderClient.lastLocation
                        .addOnSuccessListener { location ->
                            continuation.resume(location) {
                                throw it
                            }
                        }
                }
                emit(LatLng(location.latitude, location.longitude))
                kotlinx.coroutines.delay(1000L)
            }
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