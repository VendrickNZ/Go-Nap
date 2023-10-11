package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
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
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.createAlarmNode
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmList
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsScreen
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.createMapNode

class MainActivity : ComponentActivity() {

    companion object {
        private val tag: String = MainActivity::class.simpleName!!
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val updatedViewModel: GoNapViewModel by viewModels {
        GoNapViewModelFactory(
            (this.application as GoNapApplication).goNapRepository
        )
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        GoNapViewModelFactory(
            (this.application as GoNapApplication).goNapRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Coroutine for location updates
        requestLocationPermission()

        setContent {
            LocationAlarmTheme {
                MainNavigation()
            }
        }
    }

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
                updatedViewModel.startLocationUpdates(fusedLocationClient)
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

    private fun createGeoFences() {
        
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
                SettingsScreen(viewModel = settingsViewModel)
            }

            createAlarmNode(this, resources, navController)
        }
    }
}