package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmList
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.createAlarmNode
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.createMapNode
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsScreen
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : ComponentActivity() {

    companion object {
        private val tag: String = MainActivity::class.simpleName!!
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient

    private val updatedViewModel: GoNapViewModel by viewModels {
        GoNapViewModelFactory(
            (this.application as GoNapApplication).goNapRepository
        )
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onStart() {
        super.onStart()

        try {
            openFileInput("settings.json").use { fileInputStream ->
                InputStreamReader(fileInputStream).use { reader ->
                    val settings = Settings.Gson.fromJson(
                        reader, Settings::class.java
                    )
                    val app = application as GoNapApplication
                    app.state.settings = settings
                }
            }
        } catch (_: FileNotFoundException) {
            Log.e(tag, "Settings file not found, using default settings")
        } catch (e: Exception) {
            Log.e(tag, "Error reading settings, falling back to default settings: $e")
        }
    }

    override fun onStop() {
        super.onStop()

        try {
            openFileOutput("settings.json", Context.MODE_PRIVATE).use { stream ->
                OutputStreamWriter(stream).use { writer ->
                    val app = application as GoNapApplication
                    Settings.Gson.toJson(app.state.settings, writer)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error saving settings: $e")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        val something =
            if (true) checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED else false

        Log.d("DEBUGGING", something.toString())



        // Coroutine for location updates
        requestLocationPermission()

        setContent {
            LocationAlarmTheme {
                MainNavigation()
            }
        }
    }

    private fun setupGeoFencing() {
        val app = (this.application as GoNapApplication)
        app.state.addAlarmChangeListener {
            destroyGeofenceRequests()
            if (it != null) {
                createGeofenceRequest(it)
            }
        }
    }

    private fun destroyGeofenceRequests() {
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                addOnSuccessListener {
                    Log.d(tag, "Existing geofences cancelled")
                }
                addOnFailureListener {
                    Log.e(tag, "Failed to cancel geofences")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun createGeofenceRequest(alarm: Alarm) {
        val request = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(alarm.createGeofence())
        }.build()

        geofencingClient.addGeofences(request, geofencePendingIntent).run {
            addOnSuccessListener {
                Log.d(tag, "Created geofence successfully")
            }
            addOnFailureListener {
                Log.e(tag, "Failed to create geofence")
            }
        }
    }

    private fun requestLocationPermission() {
        Log.d("DEBUGGING", "In request location permission")

        val backgroundLocationResult = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // Handle the result for background location permission
        }

        val result = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            Log.d(tag, "Receive permissions: $permissions")
            val generalLocationPermissionGranted =
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) || permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                )
            Log.d("DEBUGGING", "permission granted: $generalLocationPermissionGranted")

            if (generalLocationPermissionGranted) {
                Log.d(tag, "Starting location updates...")

                // launch coroutine for location updates
                setupGeoFencing()

                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        updatedViewModel.startLocationUpdates(fusedLocationClient)
                    }
                }
            }

            // Launch the request for background location permission
            backgroundLocationResult.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        result.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    @Composable
    private fun MainNavigation() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavbar(navController)
            }
        ) { padding ->
            MainNavHost(modifier = Modifier.padding(padding), navController = navController)
        }
    }

    @Composable
    private fun BottomNavbar(navController: NavHostController) {

        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            var selectedItem by rememberSaveable {
                mutableStateOf(NavigationNodes.Map)
            }
            Log.d(tag, "Current route: $currentRoute")
            for (node in NavigationNodes.values()) {
                BottomNavigationItem(
                    selected = currentRoute?.contains(node.url) ?: false,
                    onClick = {
                        selectedItem = node
                        navController.navigate(node.url)
                    },
                    icon = node.children
                )
            }
        }
    }

    @Composable
    private fun MainNavHost(modifier: Modifier, navController: NavHostController) {

        NavHost(navController = navController, startDestination = "map") {
            composable(NavigationNodes.Alarms.url) {
                AlarmList(
                    navController = navController
                )
            }

            createMapNode(
                modifier = modifier,
                builder = this,
                viewModel = updatedViewModel,
                navController = navController
            )

            composable(NavigationNodes.Settings.url) {
                SettingsScreen(
                    modifier = modifier,
                    updatedViewModel,
                    navController
                )
            }

            createAlarmNode(this, resources, navController)
        }
    }
}