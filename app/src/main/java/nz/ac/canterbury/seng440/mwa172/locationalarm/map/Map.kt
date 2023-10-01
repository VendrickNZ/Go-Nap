package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
@SuppressLint("MissingPermission")
fun AlarmMap(
    modifier: Modifier = Modifier,
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: MapViewModel
) {

    val cameraPositionState = rememberCameraPositionState {}
//
//    fusedLocationClient.lastLocation
//        .addOnSuccessListener {
//            location = LatLng(it.latitude, it.longitude)
//            cameraPositionState.position =
//                CameraPosition.fromLatLngZoom(location, 20f)
//        }

    val liveLocation by viewModel
        .location
        .observeAsState(initial = LatLng(0.0, 0.0))


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = liveLocation)
        )
    }
}
