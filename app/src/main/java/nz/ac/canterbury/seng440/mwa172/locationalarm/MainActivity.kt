package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.AlarmMap
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.MapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme

class MainActivity : ComponentActivity() {

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


        setContent {
            LocationAlarmTheme {
                viewModel.location.observe(this, { location ->

                })

                if (hasLocationPermissions) {
                    fetchLocation()
                    AlarmMap(
                        fusedLocationClient = fusedLocationClient,
                        viewModel = viewModel
                    )
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }

                if (hasLocationPermissions) {
                    AlarmMap(
                        fusedLocationClient = fusedLocationClient,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun fetchLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                viewModel.setLocation(it.latitude, it.longitude)
            }
        }
    }

    private fun promptForGPS() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder(this).apply {
                setMessage("GPS is not enabled on your device. Enable it in the location settings.")
                setPositiveButton("Settings") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                setNegativeButton("Cancel") { _, _ -> }
                show()
            }
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LocationAlarmTheme {
        Greeting("Android")
    }
}
