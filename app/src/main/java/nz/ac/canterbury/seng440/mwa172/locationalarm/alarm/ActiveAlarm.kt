package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory

object ActiveAlarm {

    const val BaseUrl: String = "alarm/confirm_active"

    const val id: String = "id"

    fun buildUrl(id: Long): String = "$BaseUrl?${this.id}=$id"

}

fun createActiveAlarmNode(
    builder: NavGraphBuilder,
    resources: Resources,
    navController: NavController
) {
    builder.composable(
        "alarm/confirm_active?id={id}",
        arguments = listOf(
            navArgument(ActiveAlarm.id) {
                type = NavType.LongType
            }
        )
    ) { backStackEntry ->
        val id: Long = backStackEntry.arguments?.getLong(ActiveAlarm.id) ?: 0L
        val viewModel: GoNapViewModel = viewModel(
            factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).goNapRepository)
        )
        val alarm: Alarm? = viewModel.getAlarmById(id)
        ConfirmActiveAlarm(alarm, navController)
    }
}

@Composable
fun ConfirmActiveAlarm(
    alarm: Alarm?,
    navController: NavController
) {
    
}