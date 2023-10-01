package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel: ViewModel() {

    private var _location: MutableLiveData<LatLng> = MutableLiveData(
        LatLng(0.0, 0.0)
    )

    val location: LiveData<LatLng>
        get() = _location


    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}