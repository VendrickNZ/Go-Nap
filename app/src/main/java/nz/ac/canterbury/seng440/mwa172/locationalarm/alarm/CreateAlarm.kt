package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory
import nz.ac.canterbury.seng440.mwa172.locationalarm.NavigationNodes
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

object CreateAlarm {

    const val BaseUrl: String = "alarm/create"

    const val Lat: String = "lat"

    const val Long: String = "long"

    fun buildUrl(lat: Double, long: Double): String = "$BaseUrl?$Lat=$lat&$Long=$long"

}

fun createAlarmNode(
    builder: NavGraphBuilder,
    resources: Resources,
    navController: NavController
) {

    builder.composable(
        "alarm/create?lat={lat}&long={long}",
        arguments = listOf(
            navArgument(CreateAlarm.Lat) {
                type = NavType.FloatType
            },
            navArgument(CreateAlarm.Long) {
                type = NavType.FloatType
            }
        )
    ) { backStackEntry ->
        val lat: Double = backStackEntry.arguments?.getFloat(CreateAlarm.Lat)?.toDouble() ?: 0.0
        val long: Double = backStackEntry.arguments?.getFloat(CreateAlarm.Long)?.toDouble() ?: 0.0
        CreateAlarmScreen(lat, long, resources, navController)
    }

}

@Composable
fun CreateAlarmScreen(
    lat: Double,
    long: Double,
    resources: Resources,
    navController: NavController
) {

    val viewModel: GoNapViewModel = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).repository)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.primary)
                .padding(16.dp)
        ) {
            Text(
                text = resources.getString(R.string.title_create_alarm),
                style = MaterialTheme.typography.h5
            )
        }




        Row {

            OutlinedButton(
                onClick = {
                    navController.navigate(NavigationNodes.Map.url)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resources.getString(R.string.button_cancel),
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.button
                )
            }

            Button(
                onClick = {
                    viewModel.addAlarm(
                        Alarm(
                            name = "My Alarm",
                            latitude = lat,
                            longitude = long,
                            radius = 100.0
                        )
                    )
                    navController.navigate(NavigationNodes.Map.url)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resources.getString(R.string.button_create),
                    style = MaterialTheme.typography.button
                )
            }

        }
    }

}