package nz.ac.canterbury.seng440.mwa172.locationalarm

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nz.ac.canterbury.seng440.mwa172.locationalarm.theme.LocationAlarmTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            LocationAlarmTheme {

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            var selectedItem by remember {
                                mutableIntStateOf(1)
                            }
                            for (index in arrayOf(
                                (0 to "alarms"),
                                (1 to "map"),
                                (2 to "settings")
                            )) {
                                BottomNavigationItem(
                                    selected = selectedItem == index.first,
                                    onClick = {
                                        selectedItem = index.first
                                        navController.navigate(index.second)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = ""
                                        )
                                    })
                            }
                        }
                    }
                ) { padding ->
                    val context = this
                    NavHost(navController = navController, startDestination = "map") {

                        composable("alarms") {
                            Column(modifier = Modifier.padding(padding)) {
                                Text("Alarms")
                            }
                        }

                        composable("map") {
                            Column(modifier = Modifier
                                .padding(padding)
                                .fillMaxSize(0.5f)) {
                                Button(
                                    onClick = {
                                        val intent = Intent(context, MapActivity::class.java)
                                        startActivity(intent)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Go to map"
                                    )
                                }
                            }
                        }

                        composable("settings") {
                            Column(modifier = Modifier.padding(padding)) {
                                Text("settings")
                            }
                        }
                    }
                }
            }
        }
    }
}