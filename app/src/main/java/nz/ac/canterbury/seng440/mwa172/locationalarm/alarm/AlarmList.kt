package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModelFactory

@Composable
fun AlarmList() {

    val viewModel: GoNapViewModel  = viewModel(
        factory = GoNapViewModelFactory((LocalContext.current.applicationContext as GoNapApplication).repository)
    )

    val alarms = viewModel.alarms.observeAsState(initial = listOf())


    Log.d("AlarmList", "Found ${alarms.value.size} alarms")
    LazyColumn {
        itemsIndexed(alarms.value) { _: Int, alarm: Alarm ->
            Text(text = alarm.name)
        }
    }
}