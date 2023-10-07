package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.MapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.asLatLng
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme

class MapActivity : ComponentActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val hasLocationPermissions
        get() = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]

        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        requestPermissions(permissions, 100)

        // Coroutune for location updates
        lifecycleScope.launch {
            if (hasLocationPermissions) {
                Log.d("", "has permission")
                viewModel.startLocationUpdates(fusedLocationClient)
            }
        }

        setContent {
            LocationAlarmTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }

                AlarmMap(
                    viewModel = viewModel
                )
            }
        }
    }

    @Composable
    @SuppressLint("MissingPermission")
    fun AlarmMap(
        modifier: Modifier = Modifier,
        viewModel: MapViewModel
    ) {

        val cameraPositionState = rememberCameraPositionState {
            CameraPosition(LatLng(-43.5321, 172.6362), 20f, 0f, 0f)  // Default to Christchurch, NZ
        }

        val liveLocation: Location? by viewModel
            .location
            .observeAsState(initial = null)  // Default to Christchurch, NZ

        val position: LatLng = liveLocation.asLatLng()

        LaunchedEffect(liveLocation) {
            Log.d("", "Recomposing with new location: $liveLocation")
            cameraPositionState.position = CameraPosition.fromLatLngZoom(position, 20f)
        }

        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                
            }
        ) {
            MarkerComposable(
                state = MarkerState(position = position)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.crosshair),
                    contentDescription = ""
                )
            }
        }
    }

}