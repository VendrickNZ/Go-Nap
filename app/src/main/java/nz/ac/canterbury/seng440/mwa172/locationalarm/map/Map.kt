package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.CreateAlarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.asLatLng

@Composable
@SuppressLint("MissingPermission")
fun AlarmMap(
    modifier: Modifier = Modifier,
    viewModel: GoNapViewModel,
    navController: NavController
) {
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition(LatLng(-43.5321, 172.6362), 20f, 0f, 0f)  // Default to Christchurch, NZ
    }

    val liveLocation: Location? by viewModel
        .location
        .observeAsState(initial = null)  // Default to Christchurch, NZ

    val position: LatLng = liveLocation.asLatLng()

    LaunchedEffect(liveLocation) {
        Log.d("AlarmMap", "Recomposing with new location: $liveLocation")
        cameraPositionState.position = CameraPosition.fromLatLngZoom(position, 20f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {
            navController.navigate(
                CreateAlarm.buildUrl(it.latitude, it.longitude)
            )
        }
    ) {
        MarkerComposable(
            state = MarkerState(position = position)
        ) {
            Image(
                imageVector = Icons.Filled.PersonPin,
                contentDescription = ""
            )
        }
    }
}