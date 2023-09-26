package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
@SuppressLint("MissingPermission")
fun AlarmMap(
    fusedLocationClient: FusedLocationProviderClient,
    modifier: Modifier = Modifier
) {
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val cameraPositionState = rememberCameraPositionState {}

    fusedLocationClient.lastLocation
        .addOnSuccessListener {
            location = LatLng(it.latitude, it.longitude)
            cameraPositionState.position =
                CameraPosition.fromLatLngZoom(location, 20f)
        }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = location)
        )
    }
}
