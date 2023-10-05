package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

@Composable
@SuppressLint("MissingPermission")
fun AlarmMap(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel
) {

    val cameraPositionState = rememberCameraPositionState {
        CameraPosition(LatLng(-43.5321, 172.6362), 20f, 0f, 0f)  // Default to Christchurch, NZ
    }

    val liveLocation: LatLng by viewModel
        .location
        .observeAsState(initial = LatLng(-43.5321, 172.6362))  // Default to Christchurch, NZ

    LaunchedEffect(liveLocation) {
        Log.d("", "Recomposing with new location: $liveLocation")
        cameraPositionState.position = CameraPosition.fromLatLngZoom(liveLocation, 20f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        MarkerComposable(
            state = MarkerState(position = liveLocation)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.crosshair),
                contentDescription = ""
            )
        }
    }
}
