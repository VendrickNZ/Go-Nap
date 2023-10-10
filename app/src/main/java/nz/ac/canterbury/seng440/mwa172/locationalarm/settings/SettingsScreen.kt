package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val settings by viewModel.settingsFlow.observeAsState(initial = Settings())

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
                text = stringResource(R.string.title_settings),
                style = MaterialTheme.typography.h5
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        settings?.let {
            DefaultNameSetting(
                currentName = it.defaultName,
                onUpdateDefaultName = { newName ->
                    val newSettings = settings!!.copy(defaultName = newName)
                    viewModel.saveSettings(newSettings)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        settings?.defaultRadius?.let {
            DefaultRadiusSetting(
                currentRadius = it,
                onUpdateDefaultRadius = { newRadius: Double ->
                    val newSettings = settings?.copy(defaultRadius = newRadius)
                    if (newSettings != null) {
                        viewModel.saveSettings(newSettings)
                    }
                },
                commonRadii = listOf(100.0, 200.0, 500.0)
            )
        }
    }
}

@Composable
fun DefaultNameSetting(
    currentName: String,
    onUpdateDefaultName: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label_default_name),
            style = MaterialTheme.typography.h5
        )

        TextField(
            value = currentName,
            onValueChange = { onUpdateDefaultName(it) },
            modifier = Modifier
                .width(140.dp)
                .height(52.dp)
        )
    }
}

@Composable
fun DefaultRadiusSetting(
    currentRadius: Double,
    onUpdateDefaultRadius: (Double) -> Unit,
    commonRadii: List<Double>
) {

}
