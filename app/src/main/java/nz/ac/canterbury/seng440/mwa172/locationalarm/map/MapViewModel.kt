package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine

class MapViewModel : ViewModel() {

    private var _location: MutableLiveData<LatLng> = MutableLiveData(
        LatLng(-43.5321, 172.6362)  // Default to Christchurch, NZ
    )

    val location: LiveData<LatLng>
        get() = _location

    fun setLocation(lat: Double, lng: Double) {
        _location.value = LatLng(lat, lng)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(fusedLocationProviderClient: FusedLocationProviderClient) {
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    val updatedLatitude = location.latitude + (10.0 / 111139.0)
                    setLocation(updatedLatitude, location.longitude)
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