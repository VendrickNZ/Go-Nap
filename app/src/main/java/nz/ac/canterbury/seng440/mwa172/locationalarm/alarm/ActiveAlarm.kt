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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

object ActiveAlarm {

    const val BaseUrl: String = "alarm/confirm_active"

    const val id: String = "id"

    fun buildUrl(id: Long): String = "$BaseUrl?${this.id}=$id"

}

fun createActiveAlarmNode(
    builder: NavGraphBuilder,
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
        ConfirmActiveAlarm(id, navController)
    }
}

@Composable
fun ConfirmActiveAlarm(
    id: Long,
    navController: NavController
) {

    val viewModel: GoNapViewModel = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).goNapRepository)
    )
    val alarm: Alarm? by viewModel.getAlarmById(id).asLiveData().observeAsState()

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
                text = stringResource(R.string.title_confirm_active_alarm),
                style = MaterialTheme.typography.h5
            )
        }
        
        Row {
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.button_cancel))
            }
        }
    }
}