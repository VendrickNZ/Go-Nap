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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapApplication
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapState
import nz.ac.canterbury.seng440.mwa172.locationalarm.GoNapViewModel
import nz.ac.canterbury.seng440.mwa172.locationalarm.R

@Composable
fun SettingsScreen(
    modifier: Modifier,
    viewModel: GoNapViewModel,
    navController: NavController
) {
    val state: GoNapState = (LocalContext.current.applicationContext as GoNapApplication).state
    val settings: Settings = state.settings
    Log.d("SettingsScreen", "Settings: $settings")

    var tempName by rememberSaveable { mutableStateOf(settings.defaultName) }
    var tempRadius by rememberSaveable { mutableDoubleStateOf(settings.defaultRadius) }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.primary)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.title_settings),
                style = MaterialTheme.typography.h5
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


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
            isVibrationEnabled = settings.defaultVibration,
            onUpdateDefaultVibration = { newVibration ->
                state.settings = settings.copy(defaultVibration = newVibration)
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
                    state.settings = settings.copy(
                        defaultName = tempName,
                        defaultRadius = tempRadius
                    )
                    navController.navigateUp()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.button_confirm),
                    style = MaterialTheme.typography.button
                )
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
        Spacer(modifier = Modifier.width(16.dp))

        TextField(
            value = currentName,
            onValueChange = { onUpdateDefaultName(it) },
            modifier = Modifier
                .width(140.dp)
                .height(52.dp)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DefaultRadiusSetting(
    currentRadius: Double,
    onUpdateDefaultRadius: (Double) -> Unit
) {
    var textValue by remember { mutableStateOf(currentRadius.toString()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = "$textValue",
            onValueChange = { newValue ->
                textValue = newValue
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val newRadius = textValue.toDoubleOrNull()
                    if (newRadius != null) {
                        onUpdateDefaultRadius(newRadius)
                    }
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
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

