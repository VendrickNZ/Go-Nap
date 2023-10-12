package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import android.util.Log
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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

@Composable
fun SettingsScreen(viewModel: GoNapViewModel, navController: NavController) {
    val settings by viewModel.settingsFlow.observeAsState(initial = Settings())

    var tempName by remember { mutableStateOf(settings?.defaultName ?: "") }
    var tempRadius by remember { mutableDoubleStateOf(settings?.defaultRadius ?: 0.0) }

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
                currentName = tempName,
                onUpdateDefaultName = { tempName = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            DefaultRadiusSetting(
                currentRadius = tempRadius,
                onUpdateDefaultRadius = { tempRadius = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            DefaultVibrationSetting(
                isVibrationEnabled = it.defaultVibration,
                onUpdateDefaultVibration = { newVibration ->
                    val newSettings = it.copy(defaultVibration = newVibration)
                    viewModel.saveSettings(newSettings)
                }
            )
            Row {
                OutlinedButton(
                    onClick = {
                        navController.navigateUp()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.button_cancel),
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.button
                    )
                }
                Button(
                    onClick = {
                        Log.d("Testing", settings.toString() + " " + tempName + " " + tempRadius)
                        val newSettings = settings!!.copy(
                            defaultName = tempName,
                            defaultRadius = tempRadius
                        )
                        viewModel.saveSettings(newSettings)
                        navController.navigateUp()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.button_create),
                        style = MaterialTheme.typography.button
                    )
                }
            }
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
            style = MaterialTheme.typography.h5,
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
    onUpdateDefaultRadius: (Double) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label_default_radius),
            style = MaterialTheme.typography.h5,
        )

        TextField(
            value = currentRadius.toString(),
            onValueChange = {
                val newRadius = it.toDoubleOrNull()
                if (newRadius != null) {
                    onUpdateDefaultRadius(newRadius)
                }
            },
            modifier = Modifier
                .width(140.dp)
                .height(52.dp)
        )
    }
}

@Composable
fun DefaultVibrationSetting(
    isVibrationEnabled: Boolean,
    onUpdateDefaultVibration: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label_default_vibration),
            style = MaterialTheme.typography.h5,
        )

        Switch(
            checked = isVibrationEnabled,
            onCheckedChange = { onUpdateDefaultVibration(it) },
            modifier = Modifier
        )
    }
}

