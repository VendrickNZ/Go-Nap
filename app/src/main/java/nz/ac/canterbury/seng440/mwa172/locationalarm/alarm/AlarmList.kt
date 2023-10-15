package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory
import nz.ac.canterbury.seng440.mwa172.locationalarm.NavigationNodes
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

@Composable
fun AlarmList(
    navController: NavController
) {

    val viewModel: GoNapViewModel = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).goNapRepository)
    )

    val alarms: List<Alarm> by viewModel.alarms.observeAsState(initial = listOf())

    Log.d("AlarmList", "Found ${alarms.size} alarms")


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.primaryVariant)
                    .padding(16.dp),

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.title_alarm_list),
                        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                        )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        itemsIndexed(alarms) { index: Int, alarm: Alarm ->
            AlarmRow(
                index = index,
                alarm = alarm,
                navController = navController
            )
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlarmRow(
    index: Int,
    alarm: Alarm,
    navController: NavController
) {

    val swipeableState: SwipeableState<Int> = rememberSwipeableState(0)

    Row {

        Box(
            modifier = Modifier
                .weight(3f - swipeableState.progress.fraction)
        ) {
            AlarmItem(
                alarm,
                index,
                swipeableState,
                navController
            )
        }

        val trashWeight: Float = when (swipeableState.currentValue) {
            0 -> {
                if (swipeableState.isAnimationRunning)
                    swipeableState.progress.fraction
                else
                    0f
            }

            else -> swipeableState.progress.from.toFloat()
        }

        if (trashWeight > 0f) {
            Box(
                modifier = Modifier.weight(trashWeight)
            ) {
                DeleteIcon(alarm)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlarmItem(
    alarm: Alarm,
    index: Int,
    swipeableState: SwipeableState<Int>,
    navController: NavController
) {

    val color =
        if (index % 2 == 0) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant

    val squareSize = 100.dp
    val sizePx = with(LocalDensity.current) { -squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(16.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal
            )
            .clickable {
                navController.navigate(NavigationNodes.buildMapURL(alarm.latitude, alarm.longitude))
            }
    ) {
        Text(text = alarm.name)
    }
}

@Composable
fun DeleteIcon(alarm: Alarm) {

    val viewModel: GoNapViewModel = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).goNapRepository)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.error)
            .padding(16.dp)
            .clickable {
                viewModel.removeAlarm(alarm)
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.label_delete)
        )
    }
}