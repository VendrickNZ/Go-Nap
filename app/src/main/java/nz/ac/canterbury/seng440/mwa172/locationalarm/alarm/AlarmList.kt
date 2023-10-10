package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory
import nz.ac.canterbury.seng440.mwa172.locationalarm.R
import kotlin.math.roundToInt

@Composable
fun AlarmList() {

    val viewModel: GoNapViewModel = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).repository)
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
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_alarm_list),
                    style = MaterialTheme.typography.h5
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        itemsIndexed(alarms) { index: Int, alarm: Alarm ->
            AlarmItem(index = index, alarm = alarm)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlarmItem(
    index: Int,
    alarm: Alarm
) {

    val squareSize = 100.dp
    val swipeableState: SwipeableState<Int> = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { -squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    val color =
        if (index % 2 == 0) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant

    val viewModel: GoNapViewModel = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).repository)
    )

    Row {
        Box(
            modifier = Modifier
                .weight(5f - swipeableState.progress.fraction)
        ) {
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
            ) {
                Text(text = alarm.name)
            }
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.error)
                        .padding(16.dp)
                        .clickable {
                            viewModel.removeAlarm(alarm)
                        },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}