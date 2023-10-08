package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.createAlarmNode
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.AlarmMap
import nz.ac.canterbury.seng440.mwa172.locationalarm.map.MapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme

class MainActivity : ComponentActivity() {

    companion object {
        private val tag: String = MainActivity::class.simpleName!!
    }

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

        // Coroutine for location updates
        lifecycleScope.launch {
            if (hasLocationPermissions) {
                Log.d(tag, "Starting location updates...")
                viewModel.startLocationUpdates(fusedLocationClient)
            }
        }


        setContent {
            LocationAlarmTheme {
                MainNavigation()
            }
        }
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
                Column(modifier = modifier) {
                    Text("Alarms")
                }
            }

            composable(NavigationNodes.Map.url) {
                Box(modifier = modifier) {
                    AlarmMap(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }

            composable(NavigationNodes.Settings.url) {
                Column(modifier = modifier) {
                    Text("settings")
                }
            }

            createAlarmNode(this)

        }
    }
}