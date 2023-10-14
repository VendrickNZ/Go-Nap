package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapState
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.ActiveAlarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.CreateAlarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.asLatLng


fun createMapNode(
    modifier: Modifier = Modifier,
    viewModel: GoNapViewModel,
    builder: NavGraphBuilder,
    navController: NavController
) {
    builder.composable(
        "map?lat={lat},long={long}",
        arguments = listOf(
            navArgument("lat") {
                type = NavType.FloatType
                defaultValue = Float.NaN
            },
            navArgument("long") {
                type = NavType.FloatType
                defaultValue = Float.NaN
            }
        )
    ) { backStackEntry ->
        val lat: Float = backStackEntry.arguments?.getFloat("lat") ?: Float.NaN
        val long: Float = backStackEntry.arguments?.getFloat("long") ?: Float.NaN

        val pos: LatLng? =
            if (!lat.isNaN() && !long.isNaN()) LatLng(lat.toDouble(), long.toDouble()) else null

        Log.d("MapNode", "Navigating to $pos")
        AlarmMap(modifier, latLng = pos, viewModel = viewModel, navController = navController)
    }
}

@Composable
@SuppressLint("MissingPermission")
fun AlarmMap(
    modifier: Modifier = Modifier,
    latLng: LatLng? = null,
    viewModel: GoNapViewModel,
    navController: NavController
) {

    val cameraPositionState = rememberCameraPositionState {
        CameraPosition(latLng ?: LatLng(0.0, 0.0), 17f, 0f, 0f)
    }

    val liveLocation: Location? by viewModel
        .location
        .observeAsState(initial = null)

    val position: LatLng = liveLocation.asLatLng()

    LaunchedEffect(liveLocation) {
        Log.d("AlarmMap", "Recomposing with new location: $liveLocation")
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            latLng ?: position, 17f
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLongClick = {
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

        Alarms(viewModel = viewModel, navController = navController)
    }
}

@Composable
fun Alarms(
    viewModel: GoNapViewModel,
    navController: NavController
) {
    val userLocation: Location? by viewModel.location.observeAsState(initial = null)


    if (userLocation == null) {
        return
    }

    val alarms: List<Alarm> by viewModel.alarms.observeAsState(initial = listOf())

    Log.d("AlarmView", "Placing ${alarms.size} alarms")

    for (alarm in alarms) {
        AlarmView(alarm = alarm, navController = navController)
    }
}

@Composable
fun AlarmView(
    alarm: Alarm,
    navController: NavController
) {
    Log.d("AlarmView", "Placing alarm marker at ${alarm.latitude}, ${alarm.longitude}")

    val state: GoNapState = (LocalContext.current.applicationContext as GoNapApplication).state


    val alarmLocation: LatLng = alarm.location
    MarkerComposable(
        state = MarkerState(position = alarmLocation)
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            imageVector = Icons.Filled.Alarm,
            contentDescription = ""
        )
    }
    val errorColor = MaterialTheme.colors.error
    val normalColor = MaterialTheme.colors.secondary

    val color = if (alarm.id == state.activeAlarm?.id) errorColor else normalColor

    Circle(
        center = alarmLocation,
        radius = alarm.radius,
        fillColor = color.copy(alpha = 0.15f),
        strokeWidth = 5f,
        clickable = true,
        onClick = {
            Log.d("", "Selected $alarm")
            navController.navigate(ActiveAlarm.buildUrl(alarm.id))
        }
    )
}