package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel: ViewModel() {

    private var _location: MutableLiveData<LatLng> = MutableLiveData(
        LatLng(-43.5321, 172.6362)  // Default to Christchurch, NZ
    )

    val location: LiveData<LatLng>
        get() = _location

    fun setLocation(lat: Double, lng: Double) {
        _location.value = LatLng(lat, lng)
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}