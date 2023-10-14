package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.createAlarmNode
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmList
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsScreen
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.createMapNode
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings
import java.io.FileNotFoundException
import java.io.FileWriter
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
            Log.d(tag, "Settings file not found, using default settings")
        } catch (e: Exception) {
            Log.d(tag, "Error reading settings, falling back to default settings: $e")
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
            Log.d(tag, "Error saving settings: $e")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        // Coroutine for location updates
        requestLocationPermission()

        setContent {
            LocationAlarmTheme {
                MainNavigation()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestLocationPermission() {
        val result = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val generalLocationPermissionGranted =
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) || permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                )

            if (generalLocationPermissionGranted) {
                Log.d(tag, "Starting location updates...")

                // launch coroutine for location updates

                lifecycleScope.launch {
                    updatedViewModel.startLocationUpdates(fusedLocationClient)
                }
            }
        }

        result.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )
    }

    @Composable
    private fun MainNavigation() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                MainNavbar(navController)
            }
        ) { padding ->
            MainNavHost(modifier = Modifier.padding(padding), navController = navController)
        }
    }

    @Composable
    private fun MainNavbar(navController: NavHostController) {
        BottomNavigation {
            var selectedItem by remember {
                mutableStateOf(NavigationNodes.Map)
            }
            for (node in NavigationNodes.values()) {
                BottomNavigationItem(
                    selected = selectedItem == node,
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
                SettingsScreen(viewModel = updatedViewModel, navController)
            }

            createAlarmNode(this, resources, navController)
        }
    }
}