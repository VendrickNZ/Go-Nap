package nz.ac.canterbury.seng440.mwa172.locationalarm.map

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import nz.ac.canterbury.seng440.mwa172.locationalarm.R
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.CreateAlarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.asLatLng
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.BasicAlert


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
    val app = LocalContext.current.applicationContext as GoNapApplication

    val cameraPositionState = rememberCameraPositionState {
        CameraPosition(latLng ?: LatLng(0.0, 0.0), 17f, 0f, 0f)
    }

    val liveLocation: Location? by viewModel
        .location
        .observeAsState(initial = null)

    val position: LatLng = liveLocation.asLatLng()

    val selectedAlarmState: MutableState<Alarm?> = remember {
        mutableStateOf(null)
    }
    Log.d("DEBUGGING", position.toString())

    LaunchedEffect(liveLocation) {
        Log.d("AlarmMap", "Recomposing with new location: $liveLocation")
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            latLng ?: position, 17f
        )
    }

    val azimuth by viewModel.azimuth.observeAsState(0f)
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
            state = MarkerState(position = position),
            rotation = azimuth,
            anchor = Offset(0.5f, 0.5f)
        ) {
            Image(
                imageVector = Icons.Filled.Navigation,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
            )
        }

        Alarms(
            app = app,
            viewModel = viewModel,
            selectedAlarmState = selectedAlarmState
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.primary)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.tutorial)
        )
    }

    when {
        selectedAlarmState.value != null -> {
            BasicAlert(
                title = stringResource(id = R.string.title_confirm_active_alarm),
                desc = stringResource(id = R.string.desc_confirm_active_alarm),
                onDismissRequest = {
                    selectedAlarmState.value = null
                },
                onConfirmation = {
                    app.state.activeAlarm = selectedAlarmState.value
                    selectedAlarmState.value = null
                }
            )
        }
    }
}

@Composable
fun Alarms(
    app: GoNapApplication,
    viewModel: GoNapViewModel,
    selectedAlarmState: MutableState<Alarm?>
) {
    val userLocation: Location? by viewModel.location.observeAsState(initial = null)
    if (userLocation == null) {
        return
    }

    val alarms: List<Alarm> by viewModel.alarms.observeAsState(initial = listOf())


    for (alarm in alarms) {
        AlarmView(
            app = app,
            alarm = alarm,
            selectedAlarmState = selectedAlarmState
        )
    }
}

@Composable
fun AlarmView(
    app: GoNapApplication,
    alarm: Alarm,
    selectedAlarmState: MutableState<Alarm?>
) {

    val state: GoNapState = app.state

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
        radius = alarm.radius.coerceAtLeast(1.0),
        fillColor = color.copy(alpha = 0.15f),
        strokeWidth = 5f,
        clickable = true,
        onClick = {
            Log.d("", "Selected $alarm")
            selectedAlarmState.value = alarm
        }
    )
}

@Composable
fun UserIcon(viewModel: GoNapViewModel) {

}