package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel

object CreateAlarm {

    const val BaseUrl: String = "alarm/create"

    const val Lat: String = "lat"

    const val Long: String = "long"

    fun buildUrl(lat: Double, long: Double): String = "$BaseUrl?$Lat=$lat&$Long=$long"

}

fun createAlarmNode(
    builder: NavGraphBuilder,
    viewModel: GoNapViewModel
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
        CreateAlarmScreen(lat, long, viewModel)
    }

}

@Composable
fun CreateAlarmScreen(
    lat: Double,
    long: Double,
    viewModel: GoNapViewModel
) {
    Text(text = "Pressed $lat, $long")
}