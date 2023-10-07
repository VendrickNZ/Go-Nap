package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.AlarmMap
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.MapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme

class MapActivity: ComponentActivity() {


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

}